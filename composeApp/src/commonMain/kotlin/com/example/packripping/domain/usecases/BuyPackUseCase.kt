package com.example.packripping.domain.usecases

import com.example.packripping.domain.models.Card
import com.example.packripping.domain.models.Pack
import com.example.packripping.domain.randomizer.TierWeightedPicker
import kotlinx.coroutines.delay

class BuyPackUseCase(
    private val picker: TierWeightedPicker,
    private val rippingDelayMillis: Long = DEFAULT_DELAY_MILLIS,
) {
    suspend operator fun invoke(pack: Pack): Card {
        delay(rippingDelayMillis)
        return picker.pick(pack.tiers, pack.topGrails)
    }

    companion object {
        const val DEFAULT_DELAY_MILLIS: Long = 1_000
    }
}
