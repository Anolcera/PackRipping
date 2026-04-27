package com.example.packripping.testing.fakes

import kotlin.time.Clock
import kotlin.time.Instant

class FakeClock(private val instant: Instant) : Clock {
    override fun now(): Instant = instant
}
