package com.example.packripping.di

import android.app.Application
import org.koin.android.ext.koin.androidContext

fun initKoin(application: Application) {
    startCoreKoin {
        androidContext(application)
    }
}
