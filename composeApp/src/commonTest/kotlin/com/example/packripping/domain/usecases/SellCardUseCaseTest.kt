package com.example.packripping.domain.usecases

import com.example.packripping.testing.fakes.FakeClock
import com.example.packripping.testing.fakes.FakeVaultRepository
import com.example.packripping.testing.fakes.FakeWalletRepository
import com.example.packripping.testing.fixtures.PackFixtures
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Instant

class SellCardUseCaseTest {

    @Test
    fun records_a_sold_pull_and_credits_wallet_with_card_value() = runTest {
        val vault = FakeVaultRepository()
        val wallet = FakeWalletRepository()
        val now = Instant.fromEpochMilliseconds(1_761_800_000_000L)
        val sell = SellCardUseCase(vault = vault, wallet = wallet, clock = FakeClock(now))
        val card = PackFixtures.card(sku = "BAS-A", sellPriceCents = 17500)

        sell(card = card, packId = 7, packName = "Basketball Play Maker Pack")

        // Pull recorded as sold
        assertEquals(1, vault.recordedPulls.size)
        val pull = vault.recordedPulls.single()
        assertEquals("BAS-A", pull.card.sku)
        assertEquals(7, pull.packId)
        assertEquals("Basketball Play Maker Pack", pull.packName)
        assertEquals(now, pull.pulledAt)
        assertFalse(pull.isStored)

        // Wallet credited exactly once with the card's sell price
        assertEquals(listOf(17500L), wallet.credits)
        assertEquals(17500L, wallet.current)
    }
}
