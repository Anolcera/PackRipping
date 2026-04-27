package com.example.packripping.domain.randomizer

import com.example.packripping.testing.fixtures.PackFixtures
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TierWeightedPickerTest {

    @Test
    fun picks_only_from_grails_inside_rolled_tier_band() {
        val grails = listOf(
            PackFixtures.card(sku = "T1-A", sellPriceCents = 500),
            PackFixtures.card(sku = "T1-B", sellPriceCents = 550),
            PackFixtures.card(sku = "T3-A", sellPriceCents = 1200),
            PackFixtures.card(sku = "T5-A", sellPriceCents = 17500),
        )
        val picker = TierWeightedPicker(random = Random(seed = 42))

        // Run many picks; each pick must land in some tier's price band
        repeat(200) {
            val card = picker.pick(PackFixtures.tiers, grails)
            val matchingTier = PackFixtures.tiers.firstOrNull { tier ->
                card.sellPriceCents in tier.minValueCents..tier.maxValueCents
            }
            assertTrue(matchingTier != null, "card $card landed outside any tier band")
        }
    }

    @Test
    fun distribution_roughly_matches_probabilities_for_dominant_tiers() {
        val grails = listOf(
            PackFixtures.card(sku = "T1-A", sellPriceCents = 500),     // tier1 (48%)
            PackFixtures.card(sku = "T3-A", sellPriceCents = 1200),    // tier3 (38%)
        )
        // Use a tier list with only the two tiers we have grails for
        val tiers = listOf(PackFixtures.tiers[0], PackFixtures.tiers[2])  // 48 + 38 = 86
        val picker = TierWeightedPicker(random = Random(seed = 7))

        var t1Count = 0
        var t3Count = 0
        val samples = 5_000
        repeat(samples) {
            val card = picker.pick(tiers, grails)
            if (card.sku == "T1-A") t1Count++ else t3Count++
        }

        val t1Ratio = t1Count.toDouble() / samples
        val t3Ratio = t3Count.toDouble() / samples
        // Expected ratios (normalized): 48/86 = 0.558, 38/86 = 0.442
        assertTrue(t1Ratio in 0.50..0.62, "tier1 ratio $t1Ratio outside expected band")
        assertTrue(t3Ratio in 0.38..0.50, "tier3 ratio $t3Ratio outside expected band")
    }

    @Test
    fun falls_back_to_uniform_when_no_grail_fits_rolled_tier() {
        val tier = PackFixtures.tiers[4]   // tier5: $30-$175
        val grails = listOf(
            PackFixtures.card(sku = "ONLY-T1", sellPriceCents = 500),  // doesn't fit tier5
        )
        val picker = TierWeightedPicker(random = Random(seed = 1))

        val card = picker.pick(listOf(tier), grails)

        assertEquals("ONLY-T1", card.sku)
    }

    @Test
    fun throws_on_empty_tiers() {
        val picker = TierWeightedPicker(random = Random(seed = 1))
        assertFailsWith<IllegalArgumentException> {
            picker.pick(emptyList(), listOf(PackFixtures.card()))
        }
    }

    @Test
    fun throws_on_empty_grails() {
        val picker = TierWeightedPicker(random = Random(seed = 1))
        assertFailsWith<IllegalArgumentException> {
            picker.pick(PackFixtures.tiers, emptyList())
        }
    }
}
