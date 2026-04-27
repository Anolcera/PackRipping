package com.example.packripping.domain.randomizer

import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Tier
import kotlin.random.Random

class TierWeightedPicker(private val random: Random = Random.Default) {

    fun pick(tiers: List<Tier>, grails: List<Card>): Card {
        require(tiers.isNotEmpty()) { "no tiers" }
        require(grails.isNotEmpty()) { "no grails" }

        val totalProbability = tiers.sumOf { it.probabilityPct }.coerceAtLeast(Double.MIN_VALUE)
        val roll = random.nextDouble(totalProbability)

        var cumulative = 0.0
        val selectedTier = tiers.firstOrNull { tier ->
            cumulative += tier.probabilityPct
            roll < cumulative
        } ?: tiers.last()

        val candidates = grails.filter {
            it.sellPriceCents in selectedTier.minValueCents..selectedTier.maxValueCents
        }
        return (candidates.takeIf { it.isNotEmpty() } ?: grails).random(random)
    }
}
