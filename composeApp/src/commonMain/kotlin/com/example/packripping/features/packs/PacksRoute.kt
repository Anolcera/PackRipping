package com.example.packripping.features.packs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.packripping.domain.models.Pack
import com.example.packripping.features.pack_details.PackDetailsRoute
import com.example.packripping.features.pull_reveal.PullRevealRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PacksRoute(
    viewModel: PacksViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedPackId by rememberSaveable { mutableStateOf<Int?>(null) }
    var pendingReveal by remember { mutableStateOf<Pack?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is PacksNavigationEvent.GoToPackDetails ->
                    selectedPackId = event.packId
            }
        }
    }

    PacksScreen(state = state, onEvent = viewModel::onEvent)

    // Pack details sheet — only visible when no reveal is in flight, so the two
    // ModalBottomSheets never stack on top of each other (the details sheet
    // dismisses cleanly first, then the reveal sheet slides up).
    if (pendingReveal == null) {
        selectedPackId?.let { packId ->
            PackDetailsRoute(
                packId = packId,
                onDismiss = { selectedPackId = null },
                onBuy = { pack ->
                    selectedPackId = null      // dismiss the details sheet
                    pendingReveal = pack       // queue the reveal sheet
                },
            )
        }
    }

    // Reveal sheet — fresh VM per request via key(pack), with its own
    // ViewModelStore inside PullRevealRoute.
    pendingReveal?.let { pack ->
        key(pack.id) {
            PullRevealRoute(
                pack = pack,
                onDismiss = { pendingReveal = null },
            )
        }
    }
}
