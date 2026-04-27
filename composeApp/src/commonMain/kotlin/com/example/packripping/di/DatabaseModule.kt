package com.example.packripping.di

import com.example.packripping.core.database.PackRippingDatabase
import com.example.packripping.core.database.RoomDatabaseFactory
import org.koin.dsl.module

val databaseModule = module {
    single<PackRippingDatabase> { get<RoomDatabaseFactory>().create() }
    single { get<PackRippingDatabase>().pullsDao() }
    single { get<PackRippingDatabase>().walletDao() }
}
