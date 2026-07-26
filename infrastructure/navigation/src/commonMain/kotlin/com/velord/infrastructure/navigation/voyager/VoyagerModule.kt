package com.velord.infrastructure.navigation.voyager

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.infrastructure.navigation.voyager.screen.CameraRecordingVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.DemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.DialogDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.FlowSummatorVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.HintPhoneNumberVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.ModifierDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.MorphDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.MovieVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.SettingsVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.ShapeDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.TestVoyagerScreen

internal val featureMainModule: ScreenRegistry.() -> Unit get() = screenModule {
    register<SharedScreenVoyager.Test> {
        TestVoyagerScreen(it.title, it.modifier, it.onClick)
    }
}

internal val featureBottomNavigationModule: ScreenRegistry.() -> Unit get() = screenModule {
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

internal val featureDemoModule: ScreenRegistry.() -> Unit get() = screenModule {
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
    register<SharedScreenVoyager.Demo.Dialog> {
        DialogDemoVoyagerScreen
    }
}
