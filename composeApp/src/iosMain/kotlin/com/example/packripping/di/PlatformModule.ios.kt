package com.example.packripping.di

import com.example.packripping.core.database.RoomDatabaseFactory
import org.koin.dsl.module

val iosPlatformModule = module {
    single { RoomDatabaseFactory() }
}
