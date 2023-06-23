package com.velord.uicore.compose.component

import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.core.graphics.withSave

fun DrawScope.LinearGradientShaderCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    // If you want to use more than 3 colors, you can use this parameter
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    // If you want to use more than 3 colors, you must declare the position of each color
    colorsPosition: List<Float> = listOf(0f, animatedValue, 1f)
) {
    require(gradientColors.size == colorsPosition.size) {
        "The size of gradientColors and colorsPosition must be the same"
    }
    drawIntoCanvas {
        val width = size.width
        val height = size.height
        val shader = LinearGradientShader(
            from = Offset(0f, 0f),
            to = Offset(width, height),
            colors = gradientColors,
            colorStops = colorsPosition,
            tileMode = TileMode.Clamp
        )
        val paint = Paint().asFrameworkPaint()
        paint.shader = shader

        it.nativeCanvas.withSave {
            val rect = RectF(0f, 0f, width, height)
            drawRoundRect(rect, 8f, 8f, paint)
        }
    }
}