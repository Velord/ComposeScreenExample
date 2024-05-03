package com.velord.composescreenexample.ui.main.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.navigation.SharedScreen
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen

internal val MainActivity.Companion.featureMainModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreen.Test> {
            TestScreen(it.title, it.modifier, it.onClick)
        }
    }

internal val MainActivity.Companion.featureBottomNavigationModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreen.BottomNavigationTab.Camera> {
            CameraRecordingScreen
        }
        register<SharedScreen.BottomNavigationTab.Demo> {
            com.velord.feature.demo.DemoScreen
        }
        register<SharedScreen.BottomNavigationTab.Settings> {
            SettingsScreen
        }
    }

internal val MainActivity.Companion.featureDemoModule: ScreenRegistry.() -> Unit
    get() = screenModule {
        register<SharedScreen.Demo.Shape> {
            ShapeDemoScreen
        }
        register<SharedScreen.Demo.Modifier> {
            ModifierDemoScreen
        }
        register<SharedScreen.Demo.FlowSummator> {
            FlowSummatorScreen
        }
    }