package com.example.packripping.features.pack_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packripping.core.ui.theme.PackRippingTheme
import com.example.packripping.core.ui.toDollarString
import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.models.RecentPull
import com.example.packripping.domain.models.Tier
import com.example.packripping.features.pack_details.components.OddsTable
import com.example.packripping.features.pack_details.components.RecentPullsRow
import com.example.packripping.features.pack_details.components.TopGrailsRow
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackDetailsScreen(
    state: PackDetailsUiState,
    onEvent: (PackDetailsEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onEvent(PackDetailsEvent.Dismissed) },
        sheetState = sheetState,
    ) {
        PackDetailsSheetContent(state = state, onEvent = onEvent)
    }
}

@Composable
private fun PackDetailsSheetContent(
    state: PackDetailsUiState,
    onEvent: (PackDetailsEvent) -> Unit,
) {
    when {
        state.isLoading && state.pack == null -> LoadingContent()
        state.pack != null -> LoadedContent(state = state, onEvent = onEvent)
        state.errorMessage != null -> ErrorContent(
            message = state.errorMessage,
            onRetry = { onEvent(PackDetailsEvent.Refresh) },
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxWidth().height(240.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(message, textAlign = TextAlign.Center)
        Button(onClick = onRetry) { Text("Try again") }
    }
}

@Composable
private fun LoadedContent(state: PackDetailsUiState, onEvent: (PackDetailsEvent) -> Unit) {
    val pack = requireNotNull(state.pack)
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = pack.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = pack.priceCents.toDollarString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        TopGrailsRow(grails = pack.topGrails)
        OddsTable(tiers = pack.tiers)
        RecentPullsRow(pulls = pack.recentPulls)

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Button(
            onClick = { onEvent(PackDetailsEvent.BuyClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text("Buy for ${pack.priceCents.toDollarString()}")
        }
    }
}

private val sampleTiers = listOf(
    Tier(name = "tier1", probabilityPct = 48.0, minValueCents = 400, maxValueCents = 600),
    Tier(name = "tier2", probabilityPct = 7.0, minValueCents = 600, maxValueCents = 1000),
    Tier(name = "tier3", probabilityPct = 38.0, minValueCents = 1000, maxValueCents = 1500),
    Tier(name = "tier4", probabilityPct = 5.0, minValueCents = 1500, maxValueCents = 3000),
    Tier(name = "tier5", probabilityPct = 2.0, minValueCents = 3000, maxValueCents = 17500),
)

private val sampleGrails = listOf(
    Card("BAS-A", "Tyrese Maxey 2020-21 Panini Prizm", "", 17500, "10.0", "PSA"),
    Card("BAS-B", "Josh Hart 2023-24 Panini Prizm Deca", "", 15500, "9.0", "PSA"),
    Card("BAS-C", "David Robinson 2024-25 Panini Prizm", "", 14000, "9.5", "PSA"),
)

private val sampleRecentPulls = listOf(
    RecentPull(91, "Coby White 2019-20 Panini Chronicles", "", "BAS-X", "tier3", 850, Instant.fromEpochMilliseconds(0)),
    RecentPull(90, "Patrick Beverley 2020-21 Panini Mosaic", "", "BAS-Y", "tier1", 480, Instant.fromEpochMilliseconds(0)),
)

private val samplePack = Pack(
    id = 7, name = "Basketball Play Maker Pack", priceCents = 1000,
    tiers = sampleTiers,
    topGrails = sampleGrails,
    recentPulls = sampleRecentPulls,
)

@Preview(showBackground = true)
@Composable
private fun PackDetailsSheetLoadedPreview() = PackRippingTheme {
    PackDetailsSheetContent(state = PackDetailsUiState(isLoading = false, pack = samplePack), onEvent = {})
}

@Preview(showBackground = true)
@Composable
private fun PackDetailsSheetLoadingPreview() = PackRippingTheme {
    PackDetailsSheetContent(state = PackDetailsUiState(isLoading = true), onEvent = {})
}

@Preview(showBackground = true)
@Composable
private fun PackDetailsSheetErrorPreview() = PackRippingTheme {
    PackDetailsSheetContent(
        state = PackDetailsUiState(isLoading = false, errorMessage = "No connection — check your network."),
        onEvent = {},
    )
}
