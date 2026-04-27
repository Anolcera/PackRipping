package com.example.packripping.domain.usecases

import com.example.packripping.core.network.ApiResult
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.repositories.PacksRepository
import kotlinx.coroutines.flow.Flow

class GetPackDetailsUseCase(private val repository: PacksRepository) {
    operator fun invoke(id: Int): Flow<ApiResult<Pack>> = repository.getPack(id)
}
