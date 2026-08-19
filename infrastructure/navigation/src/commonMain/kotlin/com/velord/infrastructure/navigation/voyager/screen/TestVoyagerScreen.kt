package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.velord.core.resource.AppStringResource
import com.velord.infrastructure.navigation.TestScreen

/**
 * Voyager adapter for the shared navigation diagnostic surface.
 */
internal data class TestVoyagerScreen(
    val title: AppStringResource,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit = {},
) : Screen {

    @Composable
    override fun Content() {
        TestScreen(
            title,
            modifier,
            onClick,
        )
    }
}
