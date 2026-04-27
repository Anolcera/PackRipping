package com.example.packripping.core.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual class RoomDatabaseFactory(private val context: Context) {
    actual fun create(): PackRippingDatabase {
        val dbFile = context.getDatabasePath(PackRippingDatabase.NAME)
        return Room.databaseBuilder<PackRippingDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
