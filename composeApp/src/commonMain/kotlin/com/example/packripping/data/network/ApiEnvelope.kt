package com.example.packripping.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiEnvelope<T>(
    val success: Boolean,
    val data: T,
    val timestamp: String? = null,
)
