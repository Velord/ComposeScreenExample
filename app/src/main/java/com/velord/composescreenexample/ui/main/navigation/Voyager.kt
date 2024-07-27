package com.velord.composescreenexample.ui.main.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.composescreenexample.ui.screen.TestScreen
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.navigation.voyager.SharedScreenVoyager
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen

internal val MainActivity.Companion.featureMainModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreenVoyager.Test> {
            TestScreen(it.title, it.modifier, it.onClick)
        }
    }

internal val MainActivity.Companion.featureBottomNavigationModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreenVoyager.BottomNavigationTab.Camera> {
            CameraRecordingScreen
        }
        register<SharedScreenVoyager.BottomNavigationTab.Demo> {
            com.velord.feature.demo.DemoScreen
        }
        register<SharedScreenVoyager.BottomNavigationTab.Settings> {
            SettingsScreen
        }
    }

internal val MainActivity.Companion.featureDemoModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreenVoyager.Demo.Shape> {
            ShapeDemoScreen
        }
        register<SharedScreenVoyager.Demo.Modifier> {
            ModifierDemoScreen
        }
        register<SharedScreenVoyager.Demo.FlowSummator> {
            FlowSummatorScreen
        }
    }