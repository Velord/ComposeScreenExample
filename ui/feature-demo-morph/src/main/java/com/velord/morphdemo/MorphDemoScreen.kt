package com.velord.morphdemo

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object MorphDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        MorphDemoScreen()
    }
}