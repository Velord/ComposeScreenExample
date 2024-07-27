package com.velord.navigation.voyager

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.camerarecording.CameraRecordingVoyagerScreen
import com.velord.feature.demo.DemoVoyagerScreen
import com.velord.feature.movie.MovieVoyagerScreen
import com.velord.flowsummator.FlowSummatorVoyagerScreen
import com.velord.hintphonenumber.HintPhoneNumberVoyagerScreen
import com.velord.modifierdemo.ModifierDemoVoyagerScreen
import com.velord.morphdemo.MorphDemoVoyagerScreen
import com.velord.settings.SettingsVoyagerScreen
import com.velord.shapedemo.ShapeDemoVoyagerScreen

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