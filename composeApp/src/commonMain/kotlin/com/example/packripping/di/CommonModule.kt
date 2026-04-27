package com.example.packripping.di

import com.example.packripping.core.common.ErrorParser
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::ErrorParser)
}
