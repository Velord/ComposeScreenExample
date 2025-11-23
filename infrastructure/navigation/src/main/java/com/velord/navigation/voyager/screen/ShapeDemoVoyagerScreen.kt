package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.shapedemo.ShapeDemoScreen

internal object ShapeDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        ShapeDemoScreen()
    }
}