package com.example.packripping.data.packs

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class PacksApi(private val client: HttpClient) {
    suspend fun getPacks(): HttpResponse = client.get("/packs")
    suspend fun getPack(id: Int): HttpResponse = client.get("/packs/$id")
}
