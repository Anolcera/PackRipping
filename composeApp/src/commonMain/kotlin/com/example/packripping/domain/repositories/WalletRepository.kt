package com.example.packripping.domain.repositories

import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun observeBalance(): Flow<Long>
    suspend fun credit(amountCents: Long)
}
