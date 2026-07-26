package com.velord.infrastructure.navigation.voyager

import cafe.adriel.voyager.core.registry.ScreenRegistry

private var isVoyagerInitialized = false

fun initVoyager() {
    if (isVoyagerInitialized) return

    ScreenRegistry {
        featureMainModule()
        featureBottomNavigationModule()
        featureDemoModule()
    }
    isVoyagerInitialized = true
}
