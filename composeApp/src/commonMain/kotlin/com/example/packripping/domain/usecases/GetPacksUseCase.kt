package com.example.packripping.domain.usecases

import com.example.packripping.core.network.ApiResult
import com.example.packripping.domain.models.PackSummary
import com.example.packripping.domain.repositories.PacksRepository
import kotlinx.coroutines.flow.Flow

class GetPacksUseCase(private val repository: PacksRepository) {
    operator fun invoke(): Flow<ApiResult<List<PackSummary>>> = repository.getPacks()
}
