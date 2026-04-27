package com.example.packripping.features.pull_reveal

import com.example.packripping.domain.models.Card

sealed interface RevealState {
    data object Loading : RevealState
    data class Revealed(val card: Card) : RevealState
    data class Finalizing(val card: Card) : RevealState
    data class Failed(val message: String) : RevealState
}
