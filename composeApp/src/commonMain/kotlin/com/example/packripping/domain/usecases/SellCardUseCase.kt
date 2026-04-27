package com.example.packripping.domain.usecases

import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pull
import com.example.packripping.domain.repositories.VaultRepository
import com.example.packripping.domain.repositories.WalletRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SellCardUseCase(
    private val vault: VaultRepository,
    private val wallet: WalletRepository,
    private val clock: Clock = Clock.System,
) {
    suspend operator fun invoke(card: Card, packId: Int, packName: String) {
        val pull = Pull(
            id = Uuid.random().toString(),
            card = card,
            packId = packId,
            packName = packName,
            pulledAt = clock.now(),
            isStored = false,
        )
        vault.recordPull(pull)
        wallet.credit(card.sellPriceCents)
    }
}
