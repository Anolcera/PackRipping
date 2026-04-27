package com.example.packripping.data.mappers

import com.example.packripping.domain.models.Pull
import com.example.packripping.testing.fixtures.PackFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class PullMappersTest {

    @Test
    fun pull_round_trips_through_entity() {
        val original = Pull(
            id = "uuid-123",
            card = PackFixtures.card(sku = "BAS-T1", sellPriceCents = 850, name = "Coby White"),
            packId = 7,
            packName = "Basketball Play Maker Pack",
            pulledAt = Instant.fromEpochMilliseconds(1_761_803_193_278L),
            isStored = true,
        )

        val roundTripped = original.toEntity().toDomain()

        assertEquals(original, roundTripped)
    }

    @Test
    fun pull_isStored_false_round_trips() {
        val original = Pull(
            id = "uuid-456",
            card = PackFixtures.card(sku = "BAS-T2", sellPriceCents = 17500, name = "Tyrese Maxey"),
            packId = 7,
            packName = "Basketball Play Maker Pack",
            pulledAt = Instant.fromEpochMilliseconds(0),
            isStored = false,
        )

        val roundTripped = original.toEntity().toDomain()

        assertEquals(original, roundTripped)
        assertEquals(true, roundTripped.isSold)
    }
}
