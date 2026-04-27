package com.example.packripping.data.mappers

import com.example.packripping.testing.fixtures.PackFixtures
import kotlin.test.Test
import kotlin.test.assertEquals

class PackMappersTest {

    @Test
    fun packSummaryDto_to_domain_maps_fields_and_derives_max_value() {
        val dto = PackFixtures.packSummaryDto

        val summary = dto.toDomain()

        assertEquals(7, summary.id)
        assertEquals("Basketball Play Maker Pack", summary.name)
        assertEquals(1000L, summary.priceCents)
        assertEquals("https://cdn.example/pack-7.webp", summary.imageUrl)
        // max(tiers.maxPriceCents) = 17500 (tier5)
        assertEquals(17500L, summary.maxValueCents)
    }

    @Test
    fun packSummaryDto_with_no_tiers_yields_zero_max_value() {
        val dto = PackFixtures.packSummaryDto.copy(tiers = emptyList())

        val summary = dto.toDomain()

        assertEquals(0L, summary.maxValueCents)
    }

    @Test
    fun grailDto_to_domain_renames_currentValueCents_to_sellPriceCents() {
        val dto = PackFixtures.grailDto(sku = "X", valueCents = 12345)

        val card = dto.toDomain()

        assertEquals("X", card.sku)
        assertEquals(12345L, card.sellPriceCents)
        assertEquals("PSA", card.gradingCompany)
        assertEquals("10.0", card.grade)
    }

    @Test
    fun tierDto_to_domain_renames_probability_and_price_fields() {
        val dto = PackFixtures.tierDtos[4]    // tier5

        val tier = dto.toDomain()

        assertEquals("tier5", tier.name)
        assertEquals(2.0, tier.probabilityPct)
        assertEquals(3000L, tier.minValueCents)
        assertEquals(17500L, tier.maxValueCents)
    }
}
