package com.example.packripping.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.io.IOException

inline fun <reified T> safeApiCall(
    crossinline errorParser: suspend (HttpResponse) -> ApiResult<T>,
    crossinline apiCall: suspend () -> HttpResponse,
): Flow<ApiResult<T>> = flow {
    try {
        val response = apiCall()
        when {
            response.status.value == 204 -> emit(ApiResult.SuccessNobody)
            response.status.isSuccess() -> emit(ApiResult.Success(response.body<T>()))
            else -> emit(errorParser(response))
        }
    } catch (e: IOException) {
        emit(ApiResult.Error(ErrorType.Network(e)))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        emit(ApiResult.Error(ErrorType.Unknown(e)))
    }
}.flowOn(Dispatchers.Default)

inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
): Flow<ApiResult<T>> = safeApiCall<T>(
    errorParser = { it.parseErrorBody<T>() },
    apiCall = apiCall,
)

suspend inline fun <reified T> resultSafeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
): ApiResult<T> = safeApiCall<T>(apiCall = apiCall).first()

suspend inline fun <reified T> resultSafeApiCall(
    crossinline errorParser: suspend (HttpResponse) -> ApiResult<T>,
    crossinline apiCall: suspend () -> HttpResponse,
): ApiResult<T> = safeApiCall<T>(errorParser, apiCall).first()

suspend inline fun <reified T> HttpResponse.parseErrorBody(): ApiResult<T> = try {
    ApiResult.Error(ErrorType.Http(code = status.value, body = body<ErrorBody>()))
} catch (e: Exception) {
    ApiResult.Error(ErrorType.Unknown(e))
}
