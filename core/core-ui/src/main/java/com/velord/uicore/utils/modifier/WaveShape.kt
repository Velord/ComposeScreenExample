package com.velord.uicore.utils.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.velord.uicore.compose.path.wavePath

object WaveDefaults {

    val side: WaveSide = WaveSide.Top
}

enum class WaveSide {
    Top,
    Bottom;

    companion object {
        val Default = Top
    }
}

sealed class WaveSideApplier(
    open val size: Size,
    open val halfPeriod: Float,
    open val amplitude: Float
) {

    abstract fun getLinesToStartFigure(): List<Offset>

    abstract fun getLinesToFinishFigure(): List<Offset>

    class Top(
        override val size: Size,
        override val halfPeriod: Float,
        override val amplitude: Float
    ): WaveSideApplier(size = size, halfPeriod = halfPeriod, amplitude = amplitude) {

        override fun getLinesToStartFigure(): List<Offset> = listOf(
            Offset(x = -halfPeriod / 2, y = amplitude)
        )

        override fun getLinesToFinishFigure() = listOf(
            Offset(x = size.width, y = size.height),
            Offset(x = 0f, y = size.height)
        )
    }

    class Bottom(
        override val size: Size,
        override val halfPeriod: Float,
        override val amplitude: Float
    ) : WaveSideApplier(size = size, halfPeriod = halfPeriod, amplitude = amplitude) {

        override fun getLinesToStartFigure(): List<Offset> = listOf(
            Offset(x = 0f, y = size.height),
            Offset(x = -halfPeriod / 2, y = size.height - amplitude)
        )

        override fun getLinesToFinishFigure() = listOf(
            Offset(x = size.width, y = 0f),
            Offset(x = 0f, y = 0f)
        )
    }

    companion object {
        operator fun invoke(
            side: WaveSide,
            size: Size,
            halfPeriod: Float,
            amplitude: Float
        ): WaveSideApplier = when (side) {
            WaveSide.Top -> Top(size, halfPeriod = halfPeriod, amplitude = amplitude)
            WaveSide.Bottom -> Bottom(size, halfPeriod = halfPeriod, amplitude = amplitude)
        }
    }
}

class WaveShape(
    private val period: Dp,
    private val amplitude: Dp,
    private val side: WaveSide = WaveDefaults.side,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(
        wavePath(
            period = period,
            amplitude = amplitude,
            side = side,
            density = density,
            size = size
        )
    )
}

fun Modifier.waveShape(
    period: Dp,
    amplitude: Dp,
    side: WaveSide = WaveDefaults.side,
): Modifier {
    val shape = WaveShape(period = period, amplitude = amplitude, side = side)
    return this.then(Modifier.clip(shape))
}