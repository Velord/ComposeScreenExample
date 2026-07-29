package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.velord.ui.feature.demo.modifier.ModifierDemoScreen

internal object ModifierDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ModifierDemoScreen(onBackClick = { navigator.pop() })
    }
}
