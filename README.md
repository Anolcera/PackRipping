# PackRipping

A small cross-platform Kotlin Multiplatform + Compose Multiplatform app where users
browse sealed card packs, "rip" them, and decide whether to **sell** the pulled card
or **store** it in a vault. Ships Android + iOS from a single shared codebase.

## How to run

### Prerequisites
- JDK 17+ on `JAVA_HOME`
- Android SDK with API 36 platform installed (`ANDROID_HOME` or `local.properties` `sdk.dir`)
- Xcode 16+ (for iOS)

### One-time setup
Add the API base URL to `local.properties` (the file is gitignored):
```
BASE_URL=https://api.letsgooo.com
```
The build falls back to that same URL if the property is absent, so the project still
compiles out of the box.

### Android
```bash
./gradlew :composeApp:installDebug
```
Or open the project in Android Studio and run the `composeApp` configuration.

### iOS
1. Build the Compose framework once so Xcode can see it:
   ```bash
   ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
   ```
2. Open `iosApp/iosApp.xcodeproj` in Xcode, select an iOS Simulator, run.

### Tests
```bash
./gradlew :composeApp:testDebugUnitTest        # JVM/Android-side common tests
./gradlew :composeApp:iosSimulatorArm64Test    # native (iOS sim) common tests
```

## Architecture

**Mono-module KMP project** (`:composeApp`). All shared code lives under
`composeApp/src/commonMain/`, with platform actuals in `androidMain` and `iosMain`.

```
core/          common/   isDebug expect/actual, ErrorParser
               network/  Ktor HttpClient factory, ApiResult sealed, safeApiCall
               database/ Room entities, DAOs, RoomDatabaseFactory expect class
               ui/       Theme, components (SnapCarousel), formatters, custom icons
domain/        models/        plain @Immutable data classes (Pack, Card, Tier, Pull, ...)
               repositories/  pure interfaces
               usecases/      one class per use case (Get/Buy/Sell/Store/Observe...)
               randomizer/    TierWeightedPicker (pure, injectable Random)
data/          packs/         DTOs + PacksApi (Ktor) + PacksRepositoryImpl
               vault/         VaultRepositoryImpl (PullsDao)
               wallet/        WalletRepositoryImpl (WalletDao)
               mappers/       DTO → domain ↔ entity
               network/       generic ApiEnvelope<T>
features/      packs / pack_details / pull_reveal / pulls / showroom
               each: UiState, Event, NavigationEvent, ViewModel, Route, Screen, components/
navigation/    AppNavigator, route data classes (@Serializable), AppNavigationBar
di/            Koin modules + InitKoin (commonMain) + actuals on Android/iOS
```

### Key choices

- **MVI per feature.** Each ViewModel exposes `StateFlow<UiState>` + `Channel<NavigationEvent>` +
  an `onEvent(intent)` entry point. Composables are dumb renderers — no business logic in UI.
- **Clean separation UI ↔ domain ↔ data.** UI never sees DTOs or entities; domain never sees
  Ktor or Room; data never imports a `@Composable`.
- **Koin 4.2 with annotation-less DSL** (`singleOf`, `factoryOf`, `viewModelOf`). No KSP for DI
  → faster builds. KSP only runs for Room.
- **`safeApiCall` wrapper.** Every Ktor call goes through `Flow<ApiResult<T>>` (Success /
  SuccessNobody / Error{Network|Http|Custom|Unknown}). Error variants are formatted into
  user-facing copy by an `ErrorParser` injected as a Koin singleton — one place to change
  error wording for the whole app.
- **Room KMP for persistence.** The pull history (`pulls`) and wallet (`wallet` singleton row)
  are persisted across app restarts. Pack/grail data is **not** cached — both endpoints are
  cheap to refetch and freshness beats offline reach.
- **Persistent wallet table.** The wallet is its own `WalletEntity` (singleton row, `id = 0`)
  rather than derived-from-pulls. Decouples future flows (starting balance, buy-with-wallet,
  spend tracking). `credit()` is `@Transaction`-annotated for atomicity.
- **`expect class RoomDatabaseFactory`.** The platform-specific Room builder is encapsulated
  in `expect class RoomDatabaseFactory { fun create(): PackRippingDatabase }`, with platform
  actuals adding their own constructor parameters (`Context` on Android, none on iOS).
  Compile-time-enforces that every target supplies a builder, while the rest of the DI graph
  stays platform-agnostic.
- **Pack details and pull reveal are bottom sheets, not nav destinations.** `PacksRoute`
  hosts both via local state (`selectedPackId`, `pendingReveal: Pack?`). Each gets its own
  per-key `ViewModelStore` + `DisposableEffect.onDispose { clear() }` so VMs are torn down
  cleanly when the user opens a different pack or dismisses. This also lets the Material 3
  `ModalBottomSheet` provide the slide-up + scrim animations natively rather than rolling
  our own overlay.
- **Tier-weighted pull picker.** The API exposes `tiers[]` with explicit `probability`
  fields (48% / 7% / 38% / 5% / 2%). Rather than picking a card uniformly from `grails`,
  we roll against tier probabilities first, then pick uniformly among grails inside that
  tier's price band. The "Odds" UI on the bottom sheet is therefore *honest* — what you see
  is what you pull. See `domain/randomizer/TierWeightedPicker.kt`.
- **Configuration via BuildKonfig + `expect val isDebug`.** `BASE_URL` lives in
  `local.properties` (gitignored) and is generated into a `commonMain` `AppConfig` object
  by the BuildKonfig plugin. `isDebug` is **not** generated — it has real per-platform truth:
  Android reads `BuildConfig.DEBUG`, iOS reads `Platform.isDebugBinary`. Production iOS
  framework builds and Android `assembleRelease` both skip installing the Ktor `Logging`
  plugin entirely — there is no log-leak code path in release.
- **Image pipeline shares the Ktor client.** Coil 3 is configured at app start with
  `KtorNetworkFetcherFactory(httpClient = { koinInject<HttpClient>() })`, plus a 25%-of-RAM
  memory cache and a 64 MB disk cache. One TLS stack, one connection pool, consistent logging.
- **Compose stability.** All UI state classes and domain models are annotated `@Immutable`
  so Compose can skip recomposition when references are unchanged. Lists are passed via
  stable wrapper data classes; lazy lists use stable `key = { it.id }`.
- **Top-level cross-fade nav.** The `NavDisplay` only contains tab destinations
  (Packs / Pulls / Showroom), so `transitionSpec` always cross-fades — there's no spatial
  push/pop relationship to imply with a slide.

## Trade-offs

- **No real cross-table transactionality on Sell.** `SellCardUseCase` calls
  `vault.recordPull(pull)` then `wallet.credit(amount)` as separate writes. If Room fails
  between them, you'd have a recorded sale with no money credited (or vice versa). The window
  is microseconds; widening Room transactions across two repos felt like over-engineering for
  this scope. Documented as a known limitation.
- **`fallbackToDestructiveMigration(dropAllTables = true)`.** Schema is in flux; any version
  bump drops both tables on next launch. Acceptable for an in-progress assignment, **not**
  for production. A real app would write `Migration` objects.
- **Pull simulation is biased high.** Per spec, we pick from `topGrails` only (top 20 cards
  by value). The real pack pool likely has many more cards — most lower value. Our pulls
  therefore skew toward higher-value cards than a real-world rip would. Literal reading of
  the spec; documented for the reviewer.
- **Top-8 cap on grails / recent pulls.** The bottom sheet shows the top 8 of each row
  rather than the full 20 / 16. Curated feels more premium than data-dump; trivially
  configurable in `data/mappers/PackMappers.kt`.
- **Wallet is one-way.** The Buy button shows a price but never deducts — sells credit the
  wallet, but you can't actually spend it. With a starting balance of $0 there is, strictly
  speaking, no way to "buy" anything. Spec only requires the sell-credit half so I shipped
  that, but a proper shop loop is on the next-day list (see below).
- **No UI tests.** Unit-level coverage on mappers + use cases + tier picker + repository
  (with `MockEngine`) was higher-leverage in the timebox than `runComposeUiTest` plumbing.
  The MVI separation already makes UI a thin renderer over fully-tested state.

## What I'd do with another day

- **Proper wallet semantics.** The current wallet only *receives* (Sell credits it). With $0
  initial balance the user can't actually spend, so the Buy button is cosmetic. I'd add:
  (a) a starting balance / deposit flow (intro grant, "add funds" sheet, or a debug-only
  top-up), (b) Buy that actually deducts the pack price from the wallet, (c) Buy disabled
  when `walletBalance < pack.priceCents` with a clear "deposit to continue" call to action.
  The persistent `WalletEntity` is already in place for it; this is purely product wiring.
- **Write actual Room `Migration` objects** and drop the destructive fallback.
- **Cross-table transaction for Sell.** Wrap `recordPull` + `credit` in a Room transaction
  so they cannot diverge.
- **Repository test for `VaultRepositoryImpl`** with an in-memory Room database.
- **Pull-to-refresh on the Packs carousel** (`PullToRefreshBox`).
- **Card-flip animation on reveal.** The slide-up + cross-fade is in; a 3D flip from the
  mystery `?` card to the real card would land the "earned it" feeling the spec hinted at.
- **Haptics + sound on reveal** — Android via `LocalHapticFeedback`, iOS via expect/actual
  shim using `UIImpactFeedbackGenerator`.
- **Wallet badge in App.kt scaffold** so the user always sees their balance, not only on
  the Pulls tab.
- **Empty/loading skeletons** instead of spinners (perceived latency).
- **More VM-level tests** with Turbine for state-transition assertions.
- **iOS framework bundle ID** via `-Xbinary=bundleId=com.example.packripping` (cosmetic
  warning during `linkDebugFramework*`).

## AI collaboration / policy

I built this end-to-end with Claude Code (Anthropic CLI) using a stage-gated review flow:
for every architectural slice I asked for a written design *before* code was generated,
sanity-checked the proposal, called out specific changes I wanted, and only then approved
execution. The architecture is mine; the AI was a fast typist that I redirected often.

**What I implemented or owned myself:**
- **`safeApiCall`** — the Ktor wrapper, the `ApiResult` sealed hierarchy with the
  `ErrorType` variants, the explicit overload pattern that works around the Kotlin compiler's
  refusal to inline `crossinline suspend` parameters with default values, and the
  envelope-unwrapping pattern via `mapSuccess`. Ported from a Retrofit equivalent I had
  from a previous project.
- **All unit tests.** Mapper round-trips, tier picker distribution + edge cases, `BuyPack`
  with seeded `Random`, `Sell`/`Store` with fake repos and a fake `Clock`,
  `PacksRepositoryImpl` against Ktor's `MockEngine` with full envelope JSON. 19 tests
  covering the load-bearing logic.
- **The custom `SnapCarousel` component** used on the Packs home (snap-to-center, scaling,
  key-based recomposition). The default `LazyRow` reads as a flat list rather than a
  curated showcase; I built `SnapCarousel` to fix that.
- **The full image pipeline** in `App.kt` — sharing the Koin `HttpClient` with Coil's
  `KtorNetworkFetcherFactory`, plus the memory + disk caches and the gated `DebugLogger`.
- **The `ErrorParser` abstraction** — Claude originally inlined `ErrorType.toMessage()` in
  every ViewModel. I lifted it to a Koin-injected, testable parser so error wording lives
  in one place.
- **Tier probability typing** as `Double` to allow real-world fractional odds, then
  carrying the type through the picker math, the DTO, the domain model, and the UI
  formatter.
- **Persistent wallet design** — pushed for a real `WalletEntity` table with a singleton
  row and atomic `@Transaction` credit, instead of deriving balance from pulls.
- **Pack details and pull reveal as bottom sheets** — pushed back on the initial Nav3
  destination design for both. The dismiss-then-reveal flow with two stacked
  `ModalBottomSheet`s is mine.
- **`expect class RoomDatabaseFactory`** — pushed for the compile-time-enforced platform
  factory pattern instead of platform-specific top-level functions.
- **The `BASE_URL` + `isDebug` configuration split** — separating the secret (`BASE_URL`
  via BuildKonfig + `local.properties`) from the platform-truth flag (`isDebug` via
  `expect val` reading `BuildConfig.DEBUG` on Android and `Platform.isDebugBinary` on iOS).
- **The polished `Color.kt` palette and theme** — light + dark schemes, the slate/purple/
  amber tonal mix.
- **All UI refinements** — typography choices, spacing, card sizing, the error-state copy,
  the `?` mystery card animation tuning (pulse + tilt), the snap behavior on the carousel.
- **The two-VM architecture for the buy flow** — `PackDetailsViewModel` browses,
  `PullRevealViewModel` owns Buy → Sell/Store, with per-key `ViewModelStore` and
  `DisposableEffect` cleanup on each so they tear down cleanly.

**What I delegated to Claude:**
- Boilerplate Koin module wiring, Room entity/DAO definitions, DTO scaffolding, mapper
  round-trips, fixture creation.
- First drafts of feature scaffolds (UiState/Event/NavigationEvent/ViewModel/Route/Screen
  quartet) that I then trimmed and adjusted.
- The Material 3 layout drafts (sheets, lists, grids) before I refined them.
- The README structure (this file).

**What it got wrong that I had to correct:**
- KSP version `2.3.20-1.0.32` — that artifact doesn't exist; KSP moved to a unified
  `2.3.x` versioning scheme without the `<kotlin>-` prefix.
- BuildKonfig version `0.18.1` doesn't exist either (latest is `0.18.0`).
- BuildKonfig's `defaultConfigs("release")` block is for *flavors*, not AGP build types,
  so the original "set IS_DEBUG via BuildKonfig" plan never actually flipped between debug
  and release. I had to redesign the dev/prod split with `expect val isDebug` reading
  platform-native flags.
- `Logger.DEFAULT` / `Logger.SIMPLE` are JVM-only in Ktor 3.x. Replaced.
- Navigation 3 dependency was pulled from `androidx.navigation3:*` (Android-only). For KMP
  we need the JetBrains fork at `org.jetbrains.androidx.navigation3:*`, which bundles
  runtime into `navigation3-ui`.
- Claimed Koin 4.2's `factoryOf` honors default constructor parameter values — it doesn't.
  Use cases with default `Random` / `Clock` / `Long` parameters all crashed at first
  injection. I switched those to explicit `factory { ... }` lambdas.
- Initially designed pack details *and* pull reveal as Nav3 destinations — the resulting
  flow felt jarring. I reworked them into bottom sheets.
- Initial `PackDetailsScreen` rendered the reveal as an in-sheet overlay, which froze the
  bottom sheet on Buy because the wrapping `Box` wasn't `fillMaxSize` and the
  `AnimatedVisibility` collapsed the layout. I redesigned the flow as two stacked sheets.
- First Coil setup was a one-line factory with default everything — no shared `HttpClient`,
  no disk cache, no debug-gated logging. I rewrote it.
- Initial `RoomDatabaseFactory` design was platform-specific top-level functions. I pushed
  for an `expect class` so the compiler enforces every target supplies an implementation.
- Initial wallet design was "derive balance from pulls" rather than a persistent
  `WalletEntity` table.
- Initial `Tier.probabilityPct` modeled as `Int` — fine for the seeded data but loses
  precision for real fractional odds. Changed to `Double` and propagated.
- Initial `AppNavigationBar` used colored dots for tab indicators; I added a vector icon
  asset.
- Initial `PacksUiState` was a flag-soup data class (`isLoading`, `packs`, `error`) instead
  of a sealed `Loading | Loaded | Error` hierarchy. I refactored.
- Recommended drop of dark theme support at one point; I kept it.

## Time log

Started around **21:00 on 2026-04-27** and wrapped up a little after **03:00 on
2026-04-28** — roughly **6 hours of focused work in a single overnight session**.
Today was a heavy day at my day job, so the build had to fit into the evening window
after I logged off; I went in knowing the 4–6 h budget was tight and prioritised
accordingly. That constraint shaped what I built versus what I deferred to the "What
I'd do with another day" list — I leaned into architectural rigour on the load-bearing
pieces (`safeApiCall`, persistence, MVI separation, image pipeline) and consciously
shipped curated thin slices on the rest (top-8 caps on grail / recent-pull rows,
unit-only test coverage, a single bottom-sheet flow) rather than a sprawling
half-finished surface.
