package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.demo.modifier.ModifierDemoScreen

internal object ModifierDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        ModifierDemoScreen()
    }
}
