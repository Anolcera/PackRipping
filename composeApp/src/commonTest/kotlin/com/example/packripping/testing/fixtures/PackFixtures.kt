package com.example.packripping.testing.fixtures

import com.example.packripping.data.packs.dto.GrailDto
import com.example.packripping.data.packs.dto.LatestPullDto
import com.example.packripping.data.packs.dto.PackDetailsDto
import com.example.packripping.data.packs.dto.PackSummaryDto
import com.example.packripping.data.packs.dto.TierDto
import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.models.Tier

object PackFixtures {

    val tierDtos = listOf(
        TierDto(name = "tier1", probability = 48.0, minPriceCents = 400, maxPriceCents = 600),
        TierDto(name = "tier2", probability = 7.0, minPriceCents = 600, maxPriceCents = 1000),
        TierDto(name = "tier3", probability = 38.0, minPriceCents = 1000, maxPriceCents = 1500),
        TierDto(name = "tier4", probability = 5.0, minPriceCents = 1500, maxPriceCents = 3000),
        TierDto(name = "tier5", probability = 2.0, minPriceCents = 3000, maxPriceCents = 17500),
    )

    val tiers = listOf(
        Tier(name = "tier1", probabilityPct = 48.0, minValueCents = 400, maxValueCents = 600),
        Tier(name = "tier2", probabilityPct = 7.0, minValueCents = 600, maxValueCents = 1000),
        Tier(name = "tier3", probabilityPct = 38.0, minValueCents = 1000, maxValueCents = 1500),
        Tier(name = "tier4", probabilityPct = 5.0, minValueCents = 1500, maxValueCents = 3000),
        Tier(name = "tier5", probabilityPct = 2.0, minValueCents = 3000, maxValueCents = 17500),
    )

    fun grailDto(
        sku: String = "BAS-A",
        valueCents: Long = 500,
        name: String = "Card $sku",
    ) = GrailDto(
        sku = sku,
        name = name,
        imageUrl = "https://cdn.example/$sku.jpg",
        currentValueCents = valueCents,
        grade = "10.0",
        gradingCompany = "PSA",
    )

    fun card(
        sku: String = "BAS-A",
        sellPriceCents: Long = 500,
        name: String = "Card $sku",
    ) = Card(
        sku = sku,
        name = name,
        imageUrl = "https://cdn.example/$sku.jpg",
        sellPriceCents = sellPriceCents,
        grade = "10.0",
        gradingCompany = "PSA",
    )

    val packSummaryDto = PackSummaryDto(
        id = 7,
        packageName = "Basketball Play Maker Pack",
        priceCents = 1000,
        imageUrl = "https://cdn.example/pack-7.webp",
        tiers = tierDtos,
    )

    val packSummaryJson = """
        {
          "success": true,
          "data": {
            "packs": [
              {
                "id": 7,
                "packageName": "Basketball Play Maker Pack",
                "packagePriceUsdCents": 1000,
                "imageUrl": "https://cdn.example/pack-7.webp",
                "assetId": "abc",
                "tiers": [
                  { "id": "t1", "name": "tier1", "probability": 48, "numberOfCards": 1, "minPriceCents": 400, "maxPriceCents": 600, "targetFmvCents": 450, "minPrice": 4, "maxPrice": 6 },
                  { "id": "t5", "name": "tier5", "probability": 2, "numberOfCards": 1, "minPriceCents": 3000, "maxPriceCents": 17500, "targetFmvCents": 4467, "minPrice": 30, "maxPrice": 175 }
                ],
                "createdAt": "2026-04-13T08:12:40.083Z",
                "updatedAt": "2026-04-17T04:50:58.815Z",
                "isEnabled": true,
                "canOpen": true,
                "inventoryStatus": "available"
              }
            ]
          },
          "timestamp": "2026-04-28T10:00:00.000Z"
        }
    """.trimIndent()

    val packDetailsJson = """
        {
          "success": true,
          "data": {
            "id": 7,
            "packageName": "Basketball Play Maker Pack",
            "packagePriceCents": 1000,
            "packagePrice": "10.00",
            "tiers": [
              { "name": "tier1", "probability": 100, "minPriceCents": 400, "maxPriceCents": 600 }
            ],
            "grails": [
              {
                "sku": "BAS-A",
                "name": "Tyrese Maxey 2020-21 Panini Prizm",
                "imageUrl": "https://cdn.example/A.jpg",
                "categoryName": "basketball",
                "categoryDisplayName": "Basketball",
                "currentValueCents": 17500,
                "gradingCompany": "PSA",
                "grade": "10.0"
              }
            ],
            "latestPulls": [
              {
                "id": 91,
                "tierName": "tier3",
                "openedAt": "2026-04-16T14:03:49.645Z",
                "cardName": "Coby White",
                "cardImageUrl": "https://cdn.example/C.jpg",
                "cardSku": "BAS-C",
                "valueCents": 850,
                "gradingCompany": "PSA",
                "grade": "10.0"
              }
            ]
          },
          "timestamp": "2026-04-28T10:00:00.000Z"
        }
    """.trimIndent()

    fun samplePack(
        grails: List<Card> = listOf(
            card("BAS-T1", 500, "T1 card"),
            card("BAS-T1B", 550, "T1 card B"),
            card("BAS-T3", 1200, "T3 card"),
            card("BAS-T5", 17500, "T5 grail"),
        ),
    ): Pack = Pack(
        id = 7,
        name = "Basketball Play Maker Pack",
        priceCents = 1000,
        tiers = tiers,
        topGrails = grails,
        recentPulls = emptyList(),
    )
}
