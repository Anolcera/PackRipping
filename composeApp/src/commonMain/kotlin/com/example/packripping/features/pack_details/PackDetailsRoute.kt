package com.example.packripping.features.pack_details

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
fun PackDetailsRoute(
    packId: Int,
    onDismiss: () -> Unit,
    onBuy: (Pack) -> Unit,
) {
    // Per-key ViewModelStore: a fresh VM for every distinct packId,
    // and the previous VM is cleared when the user opens a different pack
    // or the sheet is dismissed entirely.
    val viewModelStore = remember(packId) { ViewModelStore() }
    DisposableEffect(packId) {
        onDispose { viewModelStore.clear() }
    }
    val sheetOwner = remember(viewModelStore) {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = viewModelStore
        }
    }

    val viewModel: PackDetailsViewModel = koinViewModel(
        viewModelStoreOwner = sheetOwner,
        key = "packDetails:$packId",
        parameters = { parametersOf(packId) },
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(packId) {
        viewModel.navigation.collect { event ->
            when (event) {
                PackDetailsNavigationEvent.Dismiss -> onDismiss()
                is PackDetailsNavigationEvent.RequestPullReveal -> onBuy(event.pack)
            }
        }
    }

    PackDetailsScreen(state = state, onEvent = viewModel::onEvent)
}
