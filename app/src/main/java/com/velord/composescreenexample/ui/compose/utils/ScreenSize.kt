package com.velord.composescreenexample.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.velord.model.utils.ScreenSize

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