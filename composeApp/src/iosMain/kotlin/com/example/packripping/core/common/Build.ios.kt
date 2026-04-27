package com.example.packripping.core.common

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
actual val isDebug: Boolean = Platform.isDebugBinary
