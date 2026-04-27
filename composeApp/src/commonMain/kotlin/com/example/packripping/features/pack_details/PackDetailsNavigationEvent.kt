package com.example.packripping.features.pack_details

import com.example.packripping.domain.models.Pack

sealed interface PackDetailsNavigationEvent {
    data object Dismiss : PackDetailsNavigationEvent
    data class RequestPullReveal(val pack: Pack) : PackDetailsNavigationEvent
}
