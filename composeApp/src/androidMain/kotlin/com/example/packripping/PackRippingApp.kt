package com.example.packripping

import android.app.Application
import com.example.packripping.di.initKoin

class PackRippingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}
