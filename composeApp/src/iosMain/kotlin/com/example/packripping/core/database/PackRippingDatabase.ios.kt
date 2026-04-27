package com.example.packripping.core.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class RoomDatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): PackRippingDatabase {
        val dbDir = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )?.path ?: error("Unable to resolve documents directory for Room database")

        return Room.databaseBuilder<PackRippingDatabase>(
            name = "$dbDir/${PackRippingDatabase.NAME}",
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
