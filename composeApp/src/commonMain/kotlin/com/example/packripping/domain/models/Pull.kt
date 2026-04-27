package com.example.packripping.domain.models

import kotlin.time.Instant

data class Pull(
    val id: String,
    val card: Card,
    val packId: Int,
    val packName: String,
    val pulledAt: Instant,
    val isStored: Boolean,
) {
    val isSold: Boolean get() = !isStored
}
