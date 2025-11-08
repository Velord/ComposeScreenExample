package com.velord.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

data class ScreenSize(
    val width: Number,
    val height: Number
)

@Composable
fun getScreenWidthAndHeightInDp(): ScreenSize {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp
    val height = configuration.screenHeightDp
    return ScreenSize(width, height)
}

@Composable
fun getScreenWidthAndHeightInPx(): ScreenSize {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val width = with(density) { configuration.screenWidthDp.dp.roundToPx() }
    val height = with(density) { configuration.screenHeightDp.dp.roundToPx() }
    return ScreenSize(width, height)
}