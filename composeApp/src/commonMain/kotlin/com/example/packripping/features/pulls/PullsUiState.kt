package com.example.packripping.features.pulls

import androidx.compose.runtime.Immutable
import com.example.packripping.domain.models.Pull

@Immutable
data class PullsUiState(
    val pulls: List<Pull> = emptyList(),
    val walletCents: Long = 0L,
    val isLoading: Boolean = true,
)
