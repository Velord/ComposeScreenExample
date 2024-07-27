package com.velord.modifierdemo

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object ModifierDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        ModifierDemoScreen()
    }
}