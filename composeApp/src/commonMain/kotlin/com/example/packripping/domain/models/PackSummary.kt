package com.example.packripping.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class PackSummary(
    val id: Int,
    val name: String,
    val priceCents: Long,
    val imageUrl: String,
    val maxValueCents: Long,
)
