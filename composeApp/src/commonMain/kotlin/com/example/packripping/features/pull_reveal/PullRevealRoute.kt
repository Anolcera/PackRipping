package com.example.packripping.features.pull_reveal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.packripping.domain.models.Pack
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PullRevealRoute(
    pack: Pack,
    onDismiss: () -> Unit,
) {
    // Per-instance ViewModelStore: every reveal request gets a brand-new VM.
    // The store is cleared when the reveal closes (or the user opens a different pack).
    val viewModelStore = remember(pack) { ViewModelStore() }
    DisposableEffect(pack) {
        onDispose { viewModelStore.clear() }
    }
    val sheetOwner = remember(viewModelStore) {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = viewModelStore
        }
    }

    val viewModel: PullRevealViewModel = koinViewModel(
        viewModelStoreOwner = sheetOwner,
        key = "pullReveal:${pack.id}",
        parameters = { parametersOf(pack) },
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(pack) {
        viewModel.navigation.collect { event ->
            when (event) {
                PullRevealNavigationEvent.Done -> onDismiss()
            }
        }
    }

    PullRevealSheet(
        state = state,
        onEvent = viewModel::onEvent,
        onDismissRequest = onDismiss,
    )
}
