package com.example.packripping.domain.models

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class RecentPull(
    val id: Int,
    val cardName: String,
    val cardImageUrl: String,
    val cardSku: String,
    val tierName: String,
    val valueCents: Long,
    val openedAt: Instant,
)
