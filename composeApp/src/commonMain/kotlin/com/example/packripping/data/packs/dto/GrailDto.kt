package com.example.packripping.data.packs.dto

import kotlinx.serialization.Serializable

@Serializable
data class GrailDto(
    val sku: String,
    val name: String,
    val imageUrl: String,
    val currentValueCents: Long,
    val grade: String,
    val gradingCompany: String,
)
