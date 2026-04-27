package com.example.packripping.features.packs

sealed interface PacksEvent {
    data object Refresh : PacksEvent
    data class PackClicked(val packId: Int) : PacksEvent
}
