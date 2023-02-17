package com.velord.backend.factory

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.velord.backend.model.BaseUrl
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 20L

fun Retrofit(
    baseUrl: BaseUrl,
    client: OkHttpClient,
    converterFactory: Converter.Factory
): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl.value)
    .addConverterFactory(converterFactory)
    .client(client)
    .build()

fun OkHttpClient(
    vararg interceptors: Interceptor
): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
    .apply {
        interceptors.forEach {
            addInterceptor(it)
        }
    }
    .build()

fun LoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

fun ConverterFactory(): Converter.Factory {
    val media: MediaType = "application/json; charset=utf-8".toMediaType()
    return json.asConverterFactory(media)
}

private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}