package com.example.packripping

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.packripping.core.common.isDebug
import com.example.packripping.core.ui.imageCacheDirectory
import com.example.packripping.core.ui.theme.PackRippingTheme
import io.ktor.client.HttpClient
import org.koin.compose.koinInject
import com.example.packripping.features.packs.PacksRoute
import com.example.packripping.features.pulls.PullsRoute
import com.example.packripping.features.showroom.ShowroomRoute
import com.example.packripping.navigation.AppNavigationBar
import com.example.packripping.navigation.AppNavigator
import com.example.packripping.navigation.PacksHome
import com.example.packripping.navigation.PullsTab
import com.example.packripping.navigation.ShowroomTab
import com.example.packripping.navigation.toTopLevelDestination

@Composable
fun App() {
    PackRippingTheme {
        val httpClient = koinInject<HttpClient>()
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components { add(KtorNetworkFetcherFactory(httpClient = { httpClient })) }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(context, 0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(imageCacheDirectory(context))
                        .maxSizeBytes(64L * 1024 * 1024)
                        .build()
                }
                .crossfade(true)
                .apply { if (isDebug) logger(DebugLogger()) }
                .build()
        }

        val backStack = remember { mutableStateListOf<Any>(PacksHome) }
        val navigator = remember { AppNavigator(backStack) }
        val currentTopLevel = navigator.currentRoute.toTopLevelDestination()

        Scaffold(
            bottomBar = {
                if (currentTopLevel != null) {
                    AppNavigationBar(
                        selected = currentTopLevel,
                        onNavigate = { destination ->
                            if (destination != currentTopLevel) {
                                navigator.navigateToTopLevel(destination)
                            }
                        },
                    )
                }
            },
        ) { padding ->
            NavDisplay(
                backStack = backStack,
                onBack = { navigator.goBack() },
                modifier = Modifier.fillMaxSize().padding(padding),
                // Every destination in this graph is a top-level tab — there is no
                // hierarchical push/pop. A simple cross-fade reads better than a
                // directional slide because the tabs are siblings, not parent/child.
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 180)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 180))
                },
                popTransitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 180)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 180))
                },
                predictivePopTransitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 180)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 180))
                },
                entryProvider = entryProvider {
                    entry<PacksHome> { PacksRoute() }
                    entry<PullsTab> { PullsRoute() }
                    entry<ShowroomTab> { ShowroomRoute() }
                },
            )
        }
    }
}
