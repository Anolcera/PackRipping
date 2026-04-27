package com.example.packripping

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform