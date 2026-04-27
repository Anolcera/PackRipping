package com.example.packripping.features.packs

import androidx.compose.runtime.Immutable
import com.example.packripping.domain.models.PackSummary

sealed interface PacksUiState {
    @Immutable
    data object Loading : PacksUiState

    @Immutable
    data class Loaded(
        val packs: List<PackSummary>,
        val isRefreshing: Boolean = false,
    ) : PacksUiState

    @Immutable
    data class Error(val message: String) : PacksUiState
}
