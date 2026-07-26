package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.demo.morph.MorphDemoScreen

internal object MorphDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        MorphDemoScreen()
    }
}
