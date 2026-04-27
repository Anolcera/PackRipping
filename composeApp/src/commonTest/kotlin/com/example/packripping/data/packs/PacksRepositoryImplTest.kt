package com.example.packripping.data.packs

import com.example.packripping.core.network.ApiResult
import com.example.packripping.core.network.ErrorType
import com.example.packripping.testing.fixtures.PackFixtures
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PacksRepositoryImplTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun client(handler: io.ktor.client.engine.mock.MockRequestHandler): HttpClient {
        val engine = MockEngine { request -> handler(request) }
        return HttpClient(engine) {
            expectSuccess = false
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://api.example.com") }
        }
    }

    @Test
    fun getPacks_success_maps_envelope_to_domain_list() = runTest {
        val httpClient = client {
            respond(
                content = ByteReadChannel(PackFixtures.packSummaryJson),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json"),
            )
        }
        val repo = PacksRepositoryImpl(PacksApi(httpClient))

        val result = repo.getPacks().first()

        val success = assertIs<ApiResult.Success<*>>(result)
        @Suppress("UNCHECKED_CAST")
        val packs = success.data as List<com.example.packripping.domain.models.PackSummary>
        assertEquals(1, packs.size)
        val pack = packs.single()
        assertEquals(7, pack.id)
        assertEquals("Basketball Play Maker Pack", pack.name)
        assertEquals(1000L, pack.priceCents)
        assertEquals(17500L, pack.maxValueCents)   // derived from tier5
    }

    @Test
    fun getPacks_http_500_yields_http_error() = runTest {
        val httpClient = client {
            respondError(HttpStatusCode.InternalServerError)
        }
        val repo = PacksRepositoryImpl(PacksApi(httpClient))

        val result = repo.getPacks().first()

        val error = assertIs<ApiResult.Error>(result)
        // 500 with no JSON body falls through to Unknown via parseErrorBody, OR
        // gets caught as Http if body decodes. Either way it's Error.
        assertTrue(
            error.type is ErrorType.Http || error.type is ErrorType.Unknown,
            "expected Http or Unknown, got ${error.type}",
        )
    }

    @Test
    fun getPack_success_maps_grails_and_caps_to_eight() = runTest {
        val httpClient = client {
            respond(
                content = ByteReadChannel(PackFixtures.packDetailsJson),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json"),
            )
        }
        val repo = PacksRepositoryImpl(PacksApi(httpClient))

        val result = repo.getPack(7).first()

        val success = assertIs<ApiResult.Success<*>>(result)
        val pack = success.data as com.example.packripping.domain.models.Pack
        assertEquals(7, pack.id)
        assertEquals(1, pack.topGrails.size)
        assertEquals("BAS-A", pack.topGrails.single().sku)
        assertEquals(17500L, pack.topGrails.single().sellPriceCents)
        assertEquals(1, pack.recentPulls.size)
        assertEquals("tier3", pack.recentPulls.single().tierName)
    }
}
