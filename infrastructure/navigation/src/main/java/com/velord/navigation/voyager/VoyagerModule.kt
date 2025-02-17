package com.velord.navigation.voyager

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.navigation.voyager.screen.CameraRecordingVoyagerScreen
import com.velord.navigation.voyager.screen.DemoVoyagerScreen
import com.velord.navigation.voyager.screen.FlowSummatorVoyagerScreen
import com.velord.navigation.voyager.screen.HintPhoneNumberVoyagerScreen
import com.velord.navigation.voyager.screen.ModifierDemoVoyagerScreen
import com.velord.navigation.voyager.screen.MorphDemoVoyagerScreen
import com.velord.navigation.voyager.screen.MovieVoyagerScreen
import com.velord.navigation.voyager.screen.SettingsVoyagerScreen
import com.velord.navigation.voyager.screen.ShapeDemoVoyagerScreen

context(Application, ScreenRegistry)
val featureMainModule: ScreenRegistry.() -> Unit get() = screenModule {
    register<SharedScreenVoyager.Test> {
        TestVoyagerScreen(it.title, it.modifier, it.onClick)
    }
}

context(Application, ScreenRegistry)
val featureBottomNavigationModule: ScreenRegistry.() -> Unit get() = screenModule {
    register<SharedScreenVoyager.BottomNavigationTab.Camera> {
        CameraRecordingVoyagerScreen
    }
    register<SharedScreenVoyager.BottomNavigationTab.Demo> {
        DemoVoyagerScreen
    }
    register<SharedScreenVoyager.BottomNavigationTab.Settings> {
        SettingsVoyagerScreen
    }
}

context(Application, ScreenRegistry)
val featureDemoModule: ScreenRegistry.() -> Unit get() = screenModule {
    register<SharedScreenVoyager.Demo.Shape> {
        ShapeDemoVoyagerScreen
    }
    register<SharedScreenVoyager.Demo.Modifier> {
        ModifierDemoVoyagerScreen
    }
    register<SharedScreenVoyager.Demo.FlowSummator> {
        FlowSummatorVoyagerScreen
    }
    register<SharedScreenVoyager.Demo.Morph> {
        MorphDemoVoyagerScreen
    }
    register<SharedScreenVoyager.Demo.HintPhoneNumber> {
        HintPhoneNumberVoyagerScreen
    }
    register<SharedScreenVoyager.Demo.Movie> {
        MovieVoyagerScreen
    }
}