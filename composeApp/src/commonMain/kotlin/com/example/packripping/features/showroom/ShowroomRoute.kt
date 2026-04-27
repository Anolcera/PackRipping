package com.example.packripping.features.showroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShowroomRoute(viewModel: ShowroomViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ShowroomScreen(state = state)
}
