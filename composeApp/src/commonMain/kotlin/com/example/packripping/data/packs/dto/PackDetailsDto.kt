package com.example.packripping.data.packs.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackDetailsDto(
    val id: Int,
    val packageName: String,
    @SerialName("packagePriceCents") val priceCents: Long,
    val tiers: List<TierDto>,
    val grails: List<GrailDto>,
    val latestPulls: List<LatestPullDto>,
)
