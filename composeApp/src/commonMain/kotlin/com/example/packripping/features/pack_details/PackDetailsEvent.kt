package com.example.packripping.features.pack_details

sealed interface PackDetailsEvent {
    data object Refresh : PackDetailsEvent
    data object BuyClicked : PackDetailsEvent
    data object Dismissed : PackDetailsEvent
    data object DismissError : PackDetailsEvent
}
