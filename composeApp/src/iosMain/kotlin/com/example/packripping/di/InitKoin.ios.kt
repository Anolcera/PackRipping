package com.example.packripping.di

fun initKoin() {
    startCoreKoin {
        modules(iosPlatformModule)
    }
}
