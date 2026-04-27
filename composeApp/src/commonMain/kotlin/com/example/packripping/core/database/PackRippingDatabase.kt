package com.example.packripping.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.packripping.core.database.entities.PullEntity
import com.example.packripping.core.database.entities.WalletEntity

@Database(
    entities = [PullEntity::class, WalletEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(PackRippingDatabaseConstructor::class)
abstract class PackRippingDatabase : RoomDatabase() {
    abstract fun pullsDao(): PullsDao
    abstract fun walletDao(): WalletDao

    companion object {
        const val NAME = "packripping.db"
    }
}

@Suppress("KotlinNoActualForExpect")
expect object PackRippingDatabaseConstructor : RoomDatabaseConstructor<PackRippingDatabase> {
    override fun initialize(): PackRippingDatabase
}
