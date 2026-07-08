package com.velord.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

data class ScreenSize(
    val width: Number,
    val height: Number,
)

@Composable
fun getScreenWidthAndHeightInDp(): ScreenSize {
    val size = LocalWindowInfo.current.containerDpSize
    val width = size.width.value
    val height = size.height.value
    return ScreenSize(width, height)
}

@Composable
fun getScreenWidthAndHeightInPx(): ScreenSize {
    val configuration = LocalWindowInfo.current.containerDpSize
    val density = LocalDensity.current
    val width = with(density) { configuration.width.roundToPx() }
    val height = with(density) { configuration.height.roundToPx() }
    return ScreenSize(width, height)
}
