package com.example.packripping.domain.usecases

import com.example.packripping.domain.models.Pull
import com.example.packripping.domain.repositories.VaultRepository
import kotlinx.coroutines.flow.Flow

class ObservePullsUseCase(private val vault: VaultRepository) {
    operator fun invoke(): Flow<List<Pull>> = vault.observeAllPulls()
}
