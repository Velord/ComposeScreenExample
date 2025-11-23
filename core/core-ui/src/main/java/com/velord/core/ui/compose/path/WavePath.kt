package com.velord.core.ui.compose.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.velord.core.ui.utils.modifier.WaveSide
import com.velord.core.ui.utils.modifier.WaveSideApplier
import kotlin.math.ceil

fun wavePath(
    period: Dp,
    amplitude: Dp,
    side: WaveSide,
    size: Size,
    density: Density,
): Path = Path().apply {
    val wavyPath = Path().apply {

        val halfPeriodInPx = with(density) { period.toPx() } / 2
        val amplitudeInPx = with(density) { amplitude.toPx() }
        val sideApplier = WaveSideApplier(
            side = side,
            size = size,
            halfPeriod = halfPeriodInPx,
            amplitude = amplitudeInPx
        )

        sideApplier.getLinesToStartFigure().forEach {
            moveTo(it.x, it.y)
        }

        repeat(
            ceil(size.width / halfPeriodInPx + 1).toInt()
        ) { i ->
            relativeQuadraticTo(
                dx1 = halfPeriodInPx / 2,
                dy1 = 2 * amplitudeInPx * (if (i % 2 == 0) 1 else -1),
                dx2 = halfPeriodInPx,
                dy2 = 0f
            )
        }

        sideApplier.getLinesToFinishFigure().forEach {
            lineTo(it.x, it.y)
        }
    }

    val boundsPath = Path().apply {
        addRect(Rect(offset = Offset(x = 0f, y = 0f), size = size))
    }

    op(wavyPath, boundsPath, PathOperation.Intersect)
}

