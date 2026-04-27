package com.example.packripping.features.showroom

import androidx.compose.runtime.Immutable
import com.example.packripping.domain.models.Pull

@Immutable
data class ShowroomUiState(
    val pulls: List<Pull> = emptyList(),
    val isLoading: Boolean = true,
)
