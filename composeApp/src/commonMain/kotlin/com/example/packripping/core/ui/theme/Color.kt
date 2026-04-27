package com.example.packripping.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Purple600 = Color(0xFF7C3AED)
private val Purple400 = Color(0xFFA78BFA)
private val Purple200 = Color(0xFFDDD6FE)
private val Amber500 = Color(0xFFF59E0B)
private val Amber300 = Color(0xFFFCD34D)
private val Slate900 = Color(0xFF0F172A)
private val Slate800 = Color(0xFF1E293B)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate50 = Color(0xFFF8FAFC)
private val Red500 = Color(0xFFEF4444)
private val Red200 = Color(0xFFFECACA)

val LightColors = lightColorScheme(
    primary = Purple600,
    onPrimary = Color.White,
    primaryContainer = Purple200,
    onPrimaryContainer = Slate900,
    secondary = Amber500,
    onSecondary = Slate900,
    secondaryContainer = Amber300,
    onSecondaryContainer = Slate900,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate200,
    onSurfaceVariant = Slate800,
    error = Red500,
    onError = Color.White,
    errorContainer = Red200,
    onErrorContainer = Slate900,
)

val DarkColors = darkColorScheme(
    primary = Purple400,
    onPrimary = Slate900,
    primaryContainer = Purple600,
    onPrimaryContainer = Color.White,
    secondary = Amber300,
    onSecondary = Slate900,
    secondaryContainer = Amber500,
    onSecondaryContainer = Slate900,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate800,
    onSurfaceVariant = Slate200,
    error = Red500,
    onError = Color.White,
    errorContainer = Red200,
    onErrorContainer = Slate900,
)
