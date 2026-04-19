package com.velord.navigation.voyager

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.velord.navigation.TestScreen
import org.jetbrains.compose.resources.StringResource

internal data class TestVoyagerScreen(
    val title: StringResource,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit = {},
) : Screen {

    @Composable
    override fun Content() {
        TestScreen(title, modifier, onClick)
    }
}
