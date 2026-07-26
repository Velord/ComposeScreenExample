package com.velord.infrastructure.di

import com.velord.data.backend.ktor.httpModule
import com.velord.data.gateway.GatewayModule
import org.koin.core.module.Module
import org.koin.ksp.generated.module

fun createCommonAppModuleRoster(): List<Module> = listOf(
    // Load manual DSL modules
    configModule,
    useCaseModule,
    viewModelModule,
    httpModule,
    bottomNavigationModule,
    // Load Annotation-based modules
    GatewayModule().module,
)
