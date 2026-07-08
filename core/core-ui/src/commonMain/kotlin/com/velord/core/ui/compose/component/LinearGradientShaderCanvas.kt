@file:Suppress("FunctionName")

package com.velord.core.ui.compose.component

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DEFAULT_CORNER_RADIUS = 8.dp

fun DrawScope.LinearGradientShaderCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    cornerRadius: Dp = DEFAULT_CORNER_RADIUS,
    gradientColorAndPosition: List<Pair<Color, Float>> = listOf(
        startColor to 0f,
        centerColor to animatedValue,
        endColor to 1f,
    ),
) {
    val width = size.width
    val height = size.height
    val cornerRadiusPx = cornerRadius.toPx()

    val colorStops = gradientColorAndPosition
        .map { colorAndPosition ->
            colorAndPosition.second to colorAndPosition.first
        }
        .toTypedArray()

    drawRoundRect(
        brush = Brush.linearGradient(
            colorStops = colorStops,
            start = Offset.Zero,
            end = Offset(width, height),
            tileMode = TileMode.Clamp,
        ),
        cornerRadius = CornerRadius(
            x = cornerRadiusPx,
            y = cornerRadiusPx,
        ),
    )
}