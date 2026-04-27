package com.example.packripping.core.common

import com.example.packripping.core.network.ErrorType

class ErrorParser {

    fun parse(error: ErrorType): String = when (error) {
        is ErrorType.Network -> {
            val base = "No connection — check your network and try again."
            if (isDebug) "$base\n[debug] ${error.throwable::class.simpleName}: ${error.throwable.message}" else base
        }
        is ErrorType.Http -> "Server error (${error.code ?: "?"}). Please try again."
        is ErrorType.Custom<*> -> "Unexpected response."
        is ErrorType.Unknown -> {
            val base = "Something went wrong."
            if (isDebug) "$base\n[debug] ${error.throwable::class.simpleName}: ${error.throwable.message}" else base
        }
    }
}
