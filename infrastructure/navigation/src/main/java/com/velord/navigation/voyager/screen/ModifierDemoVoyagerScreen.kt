package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.modifierdemo.ModifierDemoScreen

object ModifierDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        ModifierDemoScreen()
    }
}