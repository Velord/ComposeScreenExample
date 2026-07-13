@file:Suppress("MagicNumber")

package com.velord.core.ui.compose.polygon

import androidx.compose.ui.geometry.Offset
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.toRadians(): Float = this * PI.toFloat() / 180f

internal val PointZero: Offset = Offset.Zero

internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: Offset = PointZero,
): Offset = directionVectorOffset(angleRadians) * radius + center

internal fun directionVectorOffset(angleRadians: Float): Offset = Offset(
    x = cos(angleRadians.toDouble()).toFloat(),
    y = sin(angleRadians.toDouble()).toFloat(),
)

fun RoundedPolygon.Companion.heart(): RoundedPolygon {
    val radius = 1f
    val radiusSides = 0.8f
    val innerRadius = 0.1f

    val right = radialToCartesian(radiusSides, 0f.toRadians())
    val top = radialToCartesian(radius, 90f.toRadians())
    val left = radialToCartesian(radiusSides, 180f.toRadians())
    val bottomLeft = radialToCartesian(radius, 250f.toRadians())
    val innerBottom = radialToCartesian(innerRadius, 270f.toRadians())
    val bottomRight = radialToCartesian(radius, 290f.toRadians())

    val vertices = floatArrayOf(
        right.x,
        right.y,
        top.x,
        top.y,
        left.x,
        left.y,
        bottomLeft.x,
        bottomLeft.y,
        innerBottom.x,
        innerBottom.y,
        bottomRight.x,
        bottomRight.y,
    )

    val roundingNormal = 0.6f
    val roundingNone = 0f

    val rounding = listOf(
        CornerRounding(roundingNormal),
        CornerRounding(roundingNone),
        CornerRounding(roundingNormal),
        CornerRounding(roundingNormal),
        CornerRounding(roundingNone),
        CornerRounding(roundingNormal),
    )

    return RoundedPolygon(vertices = vertices, perVertexRounding = rounding)
}
