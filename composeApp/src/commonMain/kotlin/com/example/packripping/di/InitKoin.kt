package com.example.packripping.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

internal fun startCoreKoin(extra: KoinAppDeclaration? = null) = startKoin {
    extra?.invoke(this)
    modules(networkModule)
}

fun initKoin() {
    startCoreKoin()
}
