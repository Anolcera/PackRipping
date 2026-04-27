package com.example.packripping.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pulls")
data class PullEntity(
    @PrimaryKey val id: String,
    val cardSku: String,
    val cardName: String,
    val cardImageUrl: String,
    val cardSellPriceCents: Long,
    val cardGrade: String,
    val cardGradingCompany: String,
    val packId: Int,
    val packName: String,
    val pulledAtMillis: Long,
    val isStored: Boolean,
)
