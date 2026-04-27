package com.example.packripping.domain.models

data class Card(
    val sku: String,
    val name: String,
    val imageUrl: String,
    val sellPriceCents: Long,
    val grade: String,
    val gradingCompany: String,
)
