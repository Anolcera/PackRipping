package com.example.packripping.features.showroom

import com.example.packripping.domain.models.Pull

data class ShowroomUiState(
    val pulls: List<Pull> = emptyList(),
    val isLoading: Boolean = true,
)
