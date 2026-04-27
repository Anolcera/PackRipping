package com.example.packripping.di

import com.example.packripping.core.database.RoomDatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidPlatformModule = module {
    single { RoomDatabaseFactory(androidContext()) }
}
