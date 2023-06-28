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
    gradientColorAndPosition: List<Pair<Color, Float>> = listOf(
        startColor to 0f,
        centerColor to animatedValue,
        endColor to 1f
    )
) {
    drawIntoCanvas { canvas ->
        val width = size.width
        val height = size.height
        val shader = LinearGradientShader(
            from = Offset(0f, 0f),
            to = Offset(width, height),
            colors = gradientColorAndPosition.map { it.first },
            colorStops = gradientColorAndPosition.map { it.second },
            tileMode = TileMode.Clamp
        )
        val paint = Paint().asFrameworkPaint()
        paint.shader = shader

        canvas.nativeCanvas.withSave {
            val rect = RectF(0f, 0f, width, height)
            drawRoundRect(rect, 8f, 8f, paint)
        }
    }
}