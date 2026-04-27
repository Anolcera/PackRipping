package com.example.packripping.di

import com.example.packripping.domain.randomizer.TierWeightedPicker
import com.example.packripping.domain.usecases.BuyPackUseCase
import com.example.packripping.domain.usecases.GetPackDetailsUseCase
import com.example.packripping.domain.usecases.GetPacksUseCase
import com.example.packripping.domain.usecases.ObservePullsUseCase
import com.example.packripping.domain.usecases.ObserveVaultUseCase
import com.example.packripping.domain.usecases.ObserveWalletUseCase
import com.example.packripping.domain.usecases.SellCardUseCase
import com.example.packripping.domain.usecases.StoreCardUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCasesModule = module {
    // Explicit factories for classes with non-injectable constructor defaults
    // (Random, Clock, Long); Koin's factoryOf does not skip default-valued params.
    factory { TierWeightedPicker() }
    factory { BuyPackUseCase(picker = get()) }
    factory { SellCardUseCase(vault = get(), wallet = get()) }
    factory { StoreCardUseCase(vault = get()) }

    factoryOf(::GetPacksUseCase)
    factoryOf(::GetPackDetailsUseCase)
    factoryOf(::ObservePullsUseCase)
    factoryOf(::ObserveVaultUseCase)
    factoryOf(::ObserveWalletUseCase)
}
