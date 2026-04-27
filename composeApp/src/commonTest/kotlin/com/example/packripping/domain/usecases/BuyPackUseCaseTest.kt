package com.example.packripping.domain.usecases

import com.example.packripping.domain.randomizer.TierWeightedPicker
import com.example.packripping.testing.fixtures.PackFixtures
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class BuyPackUseCaseTest {

    @Test
    fun returns_a_card_from_pack_grails() = runTest {
        val pack = PackFixtures.samplePack()
        val useCase = BuyPackUseCase(
            picker = TierWeightedPicker(Random(seed = 13)),
            rippingDelayMillis = 0, //i don't need delay in tests
        )

        val card = useCase(pack)

        assertTrue(card in pack.topGrails, "buy must return a card from pack.topGrails")
    }

    @Test
    fun multiple_buys_yield_cards_only_from_grails() = runTest {
        val pack = PackFixtures.samplePack()
        val useCase = BuyPackUseCase(
            picker = TierWeightedPicker(Random(seed = 99)),
            rippingDelayMillis = 0,
        )

        repeat(50) {
            val card = useCase(pack)
            assertTrue(card in pack.topGrails)
        }
    }
}
