package com.velord.ui.feature.bottomnavigation

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bottomNavigationModule = module {
    singleOf(::BottomNavEventService)
}
