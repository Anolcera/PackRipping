package com.example.packripping.di

import com.example.packripping.core.network.createHttpClient
import com.example.packripping.core.network.httpClientEngine
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single { httpClientEngine() }
    single { createHttpClient(get(), get()) }
}
