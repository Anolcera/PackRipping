package com.example.packripping.core.network

import kotlinx.serialization.Serializable

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data object SuccessNobody : ApiResult<Nothing>()
    data class Error(val type: ErrorType) : ApiResult<Nothing>()

    fun dataOrNull(): T? = (this as? Success)?.data
}

sealed class ErrorType {
    data class Http(val code: Int?, val body: ErrorBody?) : ErrorType()
    data class Network(val throwable: Throwable) : ErrorType()
    data class Unknown(val throwable: Throwable) : ErrorType()
    data class Custom<T>(val code: Int?, val body: T?) : ErrorType()
}

@Serializable
data class ErrorBody(
    val code: String? = null,
    val errors: List<String> = emptyList(),
)

inline fun <T, R> ApiResult<T>.mapSuccess(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.SuccessNobody -> ApiResult.SuccessNobody
    is ApiResult.Error -> this
}
