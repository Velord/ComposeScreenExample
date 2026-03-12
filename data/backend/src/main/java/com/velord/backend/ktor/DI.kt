package com.velord.backend.ktor

import com.velord.backend.model.BaseUrl
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

private const val PROTOCOL_HTTPS = "https"
private const val MOVIE_HOST = "api.themoviedb.org"

private fun createMovieBaseUrl() = BaseUrl(
    protocol = PROTOCOL_HTTPS,
    host = MOVIE_HOST
)

val httpModule  = module {
    single { BaseHttpClient(get(), createMovieBaseUrl()) }
}

@Module
@ComponentScan("com.velord.backend.ktor")
class BackendModule