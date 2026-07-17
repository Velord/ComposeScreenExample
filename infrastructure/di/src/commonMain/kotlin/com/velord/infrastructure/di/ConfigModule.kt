package com.velord.infrastructure.di

import com.velord.infrastructure.config.BuildConfigResolver
import com.velord.infrastructure.config.GeneratedBuildConfigResolver
import org.koin.dsl.module

val configModule = module {
    single<BuildConfigResolver> { GeneratedBuildConfigResolver() }
}
