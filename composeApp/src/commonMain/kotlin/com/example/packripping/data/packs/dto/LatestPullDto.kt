package com.example.packripping.data.packs.dto

import kotlinx.serialization.Serializable

@Serializable
data class LatestPullDto(
    val id: Int,
    val tierName: String,
    val openedAt: String,
    val cardName: String,
    val cardImageUrl: String,
    val cardSku: String,
    val valueCents: Long,
    val grade: String,
    val gradingCompany: String,
)
