package com.velord.navigation.voyager

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.velord.navigation.TestScreen

data class TestVoyagerScreen(
    @StringRes val title: Int,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit = {},
) : Screen {

    @Composable
    override fun Content() {
        TestScreen(title, modifier, onClick)
    }
}