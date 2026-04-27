package com.example.packripping.data.packs

import com.example.packripping.core.network.ApiResult
import com.example.packripping.core.network.mapSuccess
import com.example.packripping.core.network.safeApiCall
import com.example.packripping.data.mappers.toDomain
import com.example.packripping.data.network.ApiEnvelope
import com.example.packripping.data.packs.dto.PackDetailsDto
import com.example.packripping.data.packs.dto.PackSummaryDto
import com.example.packripping.data.packs.dto.PacksListDataDto
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.models.PackSummary
import com.example.packripping.domain.repositories.PacksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PacksRepositoryImpl(private val api: PacksApi) : PacksRepository {

    override fun getPacks(): Flow<ApiResult<List<PackSummary>>> =
        safeApiCall<ApiEnvelope<PacksListDataDto>> { api.getPacks() }
            .map { result ->
                result.mapSuccess { envelope ->
                    envelope.data.packs.map(PackSummaryDto::toDomain)
                }
            }

    override fun getPack(id: Int): Flow<ApiResult<Pack>> =
        safeApiCall<ApiEnvelope<PackDetailsDto>> { api.getPack(id) }
            .map { result ->
                result.mapSuccess { envelope -> envelope.data.toDomain() }
            }
}
