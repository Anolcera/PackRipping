package com.example.packripping.features.packs

sealed interface PacksNavigationEvent {
    data class GoToPackDetails(val packId: Int) : PacksNavigationEvent
}
