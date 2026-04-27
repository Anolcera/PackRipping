package com.example.packripping.testing.fakes

import com.example.packripping.domain.models.Pull
import com.example.packripping.domain.repositories.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeVaultRepository : VaultRepository {

    private val pulls = MutableStateFlow<List<Pull>>(emptyList())
    val recordedPulls: List<Pull> get() = pulls.value
    var shouldThrowOnRecord: Throwable? = null

    override fun observeAllPulls(): Flow<List<Pull>> = pulls

    override fun observeStoredPulls(): Flow<List<Pull>> = pulls.map { list ->
        list.filter { it.isStored }
    }

    override suspend fun recordPull(pull: Pull) {
        shouldThrowOnRecord?.let { throw it }
        pulls.value = listOf(pull) + pulls.value
    }
}
