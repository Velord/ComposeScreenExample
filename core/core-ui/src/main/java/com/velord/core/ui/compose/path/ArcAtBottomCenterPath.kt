package com.velord.core.ui.compose.path

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import kotlin.math.min

private const val PERCENT_DIVISOR = 100.0f

fun arcAtBottomCenterPath(
    size: Size,
    progress: Int,
    onEdgeTouch: () -> Unit
) : Path = Path().apply {
    val centerW = size.width / 2f
    val onePercent = size.width / PERCENT_DIVISOR
    val currentProgress = onePercent * progress
    if (currentProgress > min(size.width, size.height)) {
        onEdgeTouch()
    }

    moveTo(centerW - currentProgress, size.height)
    arcTo(
        rect = Rect(
            left = centerW - currentProgress,
            top = size.height - currentProgress,
            right = centerW + currentProgress,
            bottom = size.height + currentProgress
        ),
        startAngleDegrees = 180.0f,
        sweepAngleDegrees = 180.0f,
        forceMoveTo = false
    )
    lineTo(
        x = centerW - currentProgress,
        y = size.height
    )

    close()
}