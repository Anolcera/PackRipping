package com.example.packripping.data.packs.dto

import kotlinx.serialization.Serializable

@Serializable
data class TierDto(
    val name: String,
    val probability: Double,
    val minPriceCents: Long,
    val maxPriceCents: Long,
)
