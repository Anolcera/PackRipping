package com.example.packripping.core.ui

fun Long.toDollarString(): String {
    val whole = this / 100
    val cents = (this % 100).let { if (it < 0) -it else it }
    val sign = if (this < 0 && whole == 0L) "-" else ""
    return "$sign\$$whole." + cents.toString().padStart(2, '0')
}
