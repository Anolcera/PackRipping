package com.example.packripping.domain.usecases

import com.example.packripping.domain.repositories.WalletRepository
import kotlinx.coroutines.flow.Flow

class ObserveWalletUseCase(private val wallet: WalletRepository) {
    operator fun invoke(): Flow<Long> = wallet.observeBalance()
}
