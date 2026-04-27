package com.example.packripping.core.network

import com.example.packripping.AppConfig
import com.example.packripping.core.common.isDebug
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun httpClientEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine, json: Json): HttpClient = HttpClient(engine) {
    expectSuccess = false

    install(ContentNegotiation) { json(json) }

    if (isDebug) {
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 10_000
        requestTimeoutMillis = 30_000
        socketTimeoutMillis = 30_000
    }

    defaultRequest {
        url(AppConfig.BASE_URL)
    }
}
