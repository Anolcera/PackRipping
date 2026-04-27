package com.example.packripping.di

import com.example.packripping.data.packs.PacksRepositoryImpl
import com.example.packripping.data.vault.VaultRepositoryImpl
import com.example.packripping.data.wallet.WalletRepositoryImpl
import com.example.packripping.domain.repositories.PacksRepository
import com.example.packripping.domain.repositories.VaultRepository
import com.example.packripping.domain.repositories.WalletRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf(::PacksRepositoryImpl) bind PacksRepository::class
    singleOf(::VaultRepositoryImpl) bind VaultRepository::class
    singleOf(::WalletRepositoryImpl) bind WalletRepository::class
}
