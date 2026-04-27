package com.example.packripping.data.packs.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackSummaryDto(
    val id: Int,
    val packageName: String,
    @SerialName("packagePriceUsdCents") val priceCents: Long,
    val imageUrl: String,
    val tiers: List<TierDto>,
)
