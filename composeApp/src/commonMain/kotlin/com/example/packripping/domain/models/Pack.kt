package com.example.packripping.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class Pack(
    val id: Int,
    val name: String,
    val priceCents: Long,
    val tiers: List<Tier>,
    val topGrails: List<Card>,
    val recentPulls: List<RecentPull>,
)
