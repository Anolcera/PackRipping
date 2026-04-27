package com.example.packripping.data.vault

import com.example.packripping.core.database.PullsDao
import com.example.packripping.core.database.entities.PullEntity
import com.example.packripping.data.mappers.toDomain
import com.example.packripping.data.mappers.toEntity
import com.example.packripping.domain.models.Pull
import com.example.packripping.domain.repositories.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VaultRepositoryImpl(private val dao: PullsDao) : VaultRepository {

    override fun observeAllPulls(): Flow<List<Pull>> =
        dao.observeAll().map { it.map(PullEntity::toDomain) }

    override fun observeStoredPulls(): Flow<List<Pull>> =
        dao.observeStored().map { it.map(PullEntity::toDomain) }

    override suspend fun recordPull(pull: Pull) {
        dao.insert(pull.toEntity())
    }
}
