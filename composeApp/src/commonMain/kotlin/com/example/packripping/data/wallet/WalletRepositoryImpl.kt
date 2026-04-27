package com.example.packripping.data.wallet

import com.example.packripping.core.database.WalletDao
import com.example.packripping.domain.repositories.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WalletRepositoryImpl(private val dao: WalletDao) : WalletRepository {

    override fun observeBalance(): Flow<Long> =
        dao.observeBalance().map { it ?: 0L }

    override suspend fun credit(amountCents: Long) {
        dao.credit(amountCents)
    }
}
