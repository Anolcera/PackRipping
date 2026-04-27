package com.example.packripping.domain.usecases

import com.example.packripping.testing.fakes.FakeClock
import com.example.packripping.testing.fakes.FakeVaultRepository
import com.example.packripping.testing.fixtures.PackFixtures
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class StoreCardUseCaseTest {

    @Test
    fun records_a_stored_pull_and_does_not_touch_wallet() = runTest {
        val vault = FakeVaultRepository()
        val now = Instant.fromEpochMilliseconds(1_761_800_000_000L)
        val store = StoreCardUseCase(vault = vault, clock = FakeClock(now))
        val card = PackFixtures.card(sku = "BAS-T5-A", sellPriceCents = 17500)

        store(card = card, packId = 7, packName = "Basketball Play Maker Pack")

        assertEquals(1, vault.recordedPulls.size)
        val pull = vault.recordedPulls.single()
        assertEquals("BAS-T5-A", pull.card.sku)
        assertEquals(now, pull.pulledAt)
        assertTrue(pull.isStored)
    }
}
