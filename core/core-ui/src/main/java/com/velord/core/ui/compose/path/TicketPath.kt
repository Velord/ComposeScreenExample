package com.velord.core.ui.compose.path

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path

private fun Path.cornerArc(
    rect: Rect,
    startAngleDegrees: Float,
) {
    arcTo(
        rect = rect,
        startAngleDegrees = startAngleDegrees,
        sweepAngleDegrees = -90.0f,
        forceMoveTo = false
    )
}

fun ticketPath(
    cornerRadius: Float,
    size: Size
): Path = Path().apply {
    // Top left arc
    cornerArc(
        rect = Rect(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius),
        startAngleDegrees = 90.0f,
    )
    lineTo(x = size.width - cornerRadius, y = 0f)
    // Top right arc
    cornerArc(
        rect = Rect(size.width - cornerRadius, -cornerRadius, size.width + cornerRadius, cornerRadius),
        startAngleDegrees = 180.0f,
    )
    lineTo(x = size.width, y = size.height - cornerRadius)
    // Bottom right arc
    cornerArc(
        rect = Rect(
            size.width - cornerRadius,
            size.height - cornerRadius,
            size.width + cornerRadius,
            size.height + cornerRadius
        ),
        startAngleDegrees = 270.0f,
    )
    lineTo(x = cornerRadius, y = size.height)
    // Bottom left arc
    cornerArc(
        rect = Rect(-cornerRadius, size.height - cornerRadius, cornerRadius, size.height + cornerRadius),
        startAngleDegrees = 0.0f,
    )
    lineTo(x = 0f, y = cornerRadius)

    close()
}
