package com.example.packripping.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class Tier(
    val name: String,
    val probabilityPct: Double,
    val minValueCents: Long,
    val maxValueCents: Long,
)
