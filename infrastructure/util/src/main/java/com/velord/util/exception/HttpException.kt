package com.velord.util.exception

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toBaseException(): BaseException = when (this) {
    is UnknownHostException, is ConnectException, is SocketTimeoutException -> BaseException.NoInternet
    is BaseException -> this
    is HttpException -> parseHttpException()
    else -> BaseException.Unknown
}.also {
    Log.d("BaseException", this.toString())
}

private val json = Json {
    ignoreUnknownKeys = true
}

private fun HttpException.parseHttpException(): BaseException = try {
    val errorBody: ResponseBody = response()?.errorBody() ?: error("Error body is null")
    val error: ErrorResponse = json.decodeFromString(errorBody.string())
    val errorMessage = error.errors.values.joinToString(", ")
    when (response()?.code()) {
        HttpURLConnection.HTTP_FORBIDDEN -> BaseException.Http.Client.AccessDenied(errorMessage)
        HttpURLConnection.HTTP_UNAUTHORIZED -> BaseException.Http.Client.Unauthorized(errorMessage)
        HttpURLConnection.HTTP_UNAVAILABLE -> BaseException.Http.Server.ServiceUnavailable(errorMessage)
        HttpURLConnection.HTTP_NOT_FOUND -> BaseException.Http.Client.NotFound(errorMessage)
        else -> BaseException.Unknown
    }
} catch (e: Exception) {
    Log.d("Throwable.toAppException", e.toString())
    BaseException.Unknown
}