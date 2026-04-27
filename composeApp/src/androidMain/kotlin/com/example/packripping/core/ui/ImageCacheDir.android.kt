package com.example.packripping.core.ui

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual fun imageCacheDirectory(context: PlatformContext): Path =
    context.cacheDir.resolve("coil_image_cache").toOkioPath()
