package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.morphdemo.MorphDemoScreen

object MorphDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        MorphDemoScreen()
    }
}