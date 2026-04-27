package com.example.packripping.domain.models

data class Tier(
    val name: String,
    val probabilityPct: Double,
    val minValueCents: Long,
    val maxValueCents: Long,
)
