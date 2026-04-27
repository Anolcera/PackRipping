package com.example.packripping.features.pack_details

import androidx.compose.runtime.Immutable
import com.example.packripping.domain.models.Pack

@Immutable
data class PackDetailsUiState(
    val isLoading: Boolean = true,
    val pack: Pack? = null,
    val errorMessage: String? = null,
)
