package com.example.packripping.testing.fakes

import com.example.packripping.domain.repositories.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWalletRepository : WalletRepository {

    private val balance = MutableStateFlow(0L)
    val current: Long get() = balance.value
    val credits = mutableListOf<Long>()
    var shouldThrowOnCredit: Throwable? = null

    override fun observeBalance(): Flow<Long> = balance

    override suspend fun credit(amountCents: Long) {
        shouldThrowOnCredit?.let { throw it }
        credits += amountCents
        balance.value = balance.value + amountCents
    }
}
