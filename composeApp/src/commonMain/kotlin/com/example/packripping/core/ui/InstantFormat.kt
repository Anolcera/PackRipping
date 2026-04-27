package com.example.packripping.core.ui

import kotlin.time.Instant

fun Instant.toShortLabel(): String {
    val s = toString()
    return s.take(16).replace('T', ' ')
}
