package com.example.packripping.data.mappers

import com.example.packripping.data.packs.dto.GrailDto
import com.example.packripping.data.packs.dto.LatestPullDto
import com.example.packripping.data.packs.dto.PackDetailsDto
import com.example.packripping.data.packs.dto.PackSummaryDto
import com.example.packripping.data.packs.dto.TierDto
import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.models.PackSummary
import com.example.packripping.domain.models.RecentPull
import com.example.packripping.domain.models.Tier
import kotlin.time.Instant

private const val GRAILS_DISPLAY_CAP = 8
private const val RECENT_PULLS_DISPLAY_CAP = 8

fun PackSummaryDto.toDomain() = PackSummary(
    id = id,
    name = packageName,
    priceCents = priceCents,
    imageUrl = imageUrl,
    maxValueCents = tiers.maxOfOrNull { it.maxPriceCents } ?: 0L,
)

fun PackDetailsDto.toDomain() = Pack(
    id = id,
    name = packageName,
    priceCents = priceCents,
    tiers = tiers.map(TierDto::toDomain),
    topGrails = grails.take(GRAILS_DISPLAY_CAP).map(GrailDto::toDomain),
    recentPulls = latestPulls.take(RECENT_PULLS_DISPLAY_CAP).map(LatestPullDto::toDomain),
)

fun TierDto.toDomain() = Tier(
    name = name,
    probabilityPct = probability,
    minValueCents = minPriceCents,
    maxValueCents = maxPriceCents,
)

fun GrailDto.toDomain() = Card(
    sku = sku,
    name = name,
    imageUrl = imageUrl,
    sellPriceCents = currentValueCents,
    grade = grade,
    gradingCompany = gradingCompany,
)

fun LatestPullDto.toDomain() = RecentPull(
    id = id,
    cardName = cardName,
    cardImageUrl = cardImageUrl,
    cardSku = cardSku,
    tierName = tierName,
    valueCents = valueCents,
    openedAt = Instant.parse(openedAt),
)
