package com.example.packripping.core.ui

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun imageCacheDirectory(context: PlatformContext): Path {
    val urls = NSFileManager.defaultManager.URLsForDirectory(NSCachesDirectory, NSUserDomainMask)
    val cachesUrl = urls.first() as NSURL
    val dir = cachesUrl.URLByAppendingPathComponent("coil_image_cache")!!.path!!
    return dir.toPath()
}
