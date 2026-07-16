package com.velord.ui.feature.bottomnavigation

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bottomNavigationModule = module {
    includes(bottomNavigationPlatformModule)
    singleOf(::BottomNavEventService)
}

internal expect val bottomNavigationPlatformModule: Module