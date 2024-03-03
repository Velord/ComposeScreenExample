package com.velord.uicore.compose.polygon

import android.graphics.PointF
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal val PointZero = PointF(0f, 0f)
internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))

fun RoundedPolygon.Companion.heart(): RoundedPolygon {
    val radius = 1f
    val radiusSides = 0.8f
    val innerRadius = 0.1f
    val vertices = floatArrayOf(
        radialToCartesian(radiusSides, 0f.toRadians()).x,
        radialToCartesian(radiusSides, 0f.toRadians()).y,
        radialToCartesian(radius, 90f.toRadians()).x,
        radialToCartesian(radius, 90f.toRadians()).y,
        radialToCartesian(radiusSides, 180f.toRadians()).x,
        radialToCartesian(radiusSides, 180f.toRadians()).y,
        radialToCartesian(radius, 250f.toRadians()).x,
        radialToCartesian(radius, 250f.toRadians()).y,
        radialToCartesian(innerRadius, 270f.toRadians()).x,
        radialToCartesian(innerRadius, 270f.toRadians()).y,
        radialToCartesian(radius, 290f.toRadians()).x,
        radialToCartesian(radius, 290f.toRadians()).y,
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

    return RoundedPolygon(
        vertices = vertices,
        perVertexRounding = rounding
    )
}