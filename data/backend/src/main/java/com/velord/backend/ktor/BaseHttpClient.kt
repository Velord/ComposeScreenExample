package com.velord.backend.ktor

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val DEVICE_TIME_HEADER = "deviceTime"
private const val TIMEOUT = 20000L

class BaseHttpClient(
    context: Context,
    private val baseUrl: String
) {

    private val client: HttpClient

    init {
        client = configureClient()
    }

    private fun configureClient() = HttpClient(OkHttp).config {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
            filter { request ->
                request.url.host.contains("fora")
            }
        }
        expectSuccess = true
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    allowStructuredMapKeys = true
                    prettyPrint = true
                    useArrayPolymorphism = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)

            url.protocol = URLProtocol.HTTPS
            url(baseUrl)

            header(DEVICE_TIME_HEADER, System.currentTimeMillis().toString())
        }
    }

    fun close() {
        client.close()
    }

    fun getClient() = client

    suspend fun post(block: HttpRequestBuilder.() -> Unit): HttpResponse = client.post { block() }
}