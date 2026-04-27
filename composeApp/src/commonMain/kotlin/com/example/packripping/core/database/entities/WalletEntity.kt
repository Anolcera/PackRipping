package com.example.packripping.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
data class WalletEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val balanceCents: Long,
) {
    companion object {
        const val SINGLETON_ID = 0
    }
}
