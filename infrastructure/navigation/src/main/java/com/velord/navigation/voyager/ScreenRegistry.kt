package com.velord.navigation.voyager

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry

context(Application)
fun initVoyager() = ScreenRegistry {
    (featureMainModule)()
    (featureBottomNavigationModule)()
    (featureDemoModule)()
}