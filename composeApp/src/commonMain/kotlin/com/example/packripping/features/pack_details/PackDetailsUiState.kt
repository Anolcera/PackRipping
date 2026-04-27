package com.example.packripping.features.pack_details

import com.example.packripping.domain.models.Pack

data class PackDetailsUiState(
    val isLoading: Boolean = true,
    val pack: Pack? = null,
    val errorMessage: String? = null,
)
