package com.example.packripping.data.packs.dto

import kotlinx.serialization.Serializable

@Serializable
data class PacksListDataDto(
    val packs: List<PackSummaryDto>,
)
