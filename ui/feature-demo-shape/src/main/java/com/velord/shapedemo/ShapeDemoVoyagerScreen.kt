package com.velord.shapedemo

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object ShapeDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        ShapeDemoScreen()
    }
}