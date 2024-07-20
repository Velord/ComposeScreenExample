package com.velord.backend.ktor

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

private const val MOVIE_URL = "https://api.themoviedb.org/"

val httpModule  = module {
    single { BaseHttpClient(get(), MOVIE_URL) }
}

@Module
@ComponentScan("com.velord.backend.ktor")
class BackendModule


