package com.velord.composescreenexample.ui.main.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.camerarecording.CameraRecordingVoyagerScreen
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.composescreenexample.ui.screen.TestVoyagerScreen
import com.velord.feature.demo.DemoVoyagerScreen
import com.velord.feature.movie.MovieVoyagerScreen
import com.velord.flowsummator.FlowSummatorVoyagerScreen
import com.velord.hintphonenumber.HintPhoneNumberVoyagerScreen
import com.velord.modifierdemo.ModifierDemoVoyagerScreen
import com.velord.navigation.voyager.SharedScreenVoyager
import com.velord.settings.SettingsVoyagerScreen
import com.velord.shapedemo.ShapeDemoVoyagerScreen

internal val MainActivity.Companion.featureMainModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreenVoyager.Test> {
            TestVoyagerScreen(it.title, it.modifier, it.onClick)
        }
    }

internal val MainActivity.Companion.featureBottomNavigationModule: ScreenRegistry.() -> Unit
    get() = screenModule {
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

internal val MainActivity.Companion.featureDemoModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreenVoyager.Demo.Shape> {
            ShapeDemoVoyagerScreen
        }
        register<SharedScreenVoyager.Demo.Modifier> {
            ModifierDemoVoyagerScreen
        }
        register<SharedScreenVoyager.Demo.FlowSummator> {
            FlowSummatorVoyagerScreen
        }
        register<SharedScreenVoyager.Demo.HintPhoneNumber> {
            HintPhoneNumberVoyagerScreen
        }
        register<SharedScreenVoyager.Demo.Movie> {
            MovieVoyagerScreen
        }
    }