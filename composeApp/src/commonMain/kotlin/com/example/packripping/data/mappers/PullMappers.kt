package com.example.packripping.data.mappers

import com.example.packripping.core.database.entities.PullEntity
import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pull
import kotlin.time.Instant

fun Pull.toEntity() = PullEntity(
    id = id,
    cardSku = card.sku,
    cardName = card.name,
    cardImageUrl = card.imageUrl,
    cardSellPriceCents = card.sellPriceCents,
    cardGrade = card.grade,
    cardGradingCompany = card.gradingCompany,
    packId = packId,
    packName = packName,
    pulledAtMillis = pulledAt.toEpochMilliseconds(),
    isStored = isStored,
)

fun PullEntity.toDomain() = Pull(
    id = id,
    card = Card(
        sku = cardSku,
        name = cardName,
        imageUrl = cardImageUrl,
        sellPriceCents = cardSellPriceCents,
        grade = cardGrade,
        gradingCompany = cardGradingCompany,
    ),
    packId = packId,
    packName = packName,
    pulledAt = Instant.fromEpochMilliseconds(pulledAtMillis),
    isStored = isStored,
)
