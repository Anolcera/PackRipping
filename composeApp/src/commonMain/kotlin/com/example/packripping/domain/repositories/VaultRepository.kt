package com.example.packripping.domain.repositories

import com.example.packripping.domain.models.Pull
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    fun observeAllPulls(): Flow<List<Pull>>
    fun observeStoredPulls(): Flow<List<Pull>>
    suspend fun recordPull(pull: Pull)
}
