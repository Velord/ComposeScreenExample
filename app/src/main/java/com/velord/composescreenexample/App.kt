package com.velord.composescreenexample

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.velord.composescreenexample.ui.main.MainActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Navigation
        ScreenRegistry {
            (MainActivity.featureMainModule)()
            (MainActivity.featureBottomNavigationModule)()
            (MainActivity.featureDemoModule)()
        }
    }
}