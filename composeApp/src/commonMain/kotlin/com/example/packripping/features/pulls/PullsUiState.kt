package com.example.packripping.features.pulls

import com.example.packripping.domain.models.Pull

data class PullsUiState(
    val pulls: List<Pull> = emptyList(),
    val walletCents: Long = 0L,
    val isLoading: Boolean = true,
)
