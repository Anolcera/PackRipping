package com.example.packripping.domain.repositories

import com.example.packripping.core.network.ApiResult
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.models.PackSummary
import kotlinx.coroutines.flow.Flow

interface PacksRepository {
    fun getPacks(): Flow<ApiResult<List<PackSummary>>>
    fun getPack(id: Int): Flow<ApiResult<Pack>>
}
