package com.velord.core.ui.utils.modifier

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.min

private const val HIGHLIGHT_DURATION = 3000L
/**
 * A [Modifier] that draws a border around elements that are recomposing. The border increases in
 * size and interpolates from red to green as more recompositions occur before a timeout.
 */
// Use a single instance + @Stable to ensure that recompositions can enable skipping optimizations
// Modifier.composed will still remember unique data per call site.
@Stable
fun Modifier.recomposeHighlighter(
    highlightDuration: Long = HIGHLIGHT_DURATION,
    initialColor: Color = Color.Blue,
    okColor: Color = Color.Green,
    warningColor: Color = Color.Yellow,
    errorColor: Color = Color.Red
): Modifier = this.composed(
    inspectorInfo = debugInspectorInfo { name = "recomposeHighlighter" }
) {
    // The total number of compositions that have occurred. We're not using a State<> here be
    // able to read/write the value without invalidating (which would cause infinite
    // recomposition).
    val totalCompositions = remember { arrayOf(0L) }
    totalCompositions[0]++

    // The value of totalCompositions at the last timeout.
    val totalCompositionsAtLastTimeout = remember { mutableLongStateOf(0L) }

    // Start the timeout, and reset everytime there's a recomposition. (Using totalCompositions
    // as the key is really just to cause the timer to restart every composition).
    LaunchedEffect(totalCompositions[0]) {
        delay(highlightDuration)
        totalCompositionsAtLastTimeout.value = totalCompositions[0]
    }

    Modifier.drawWithCache {
        onDrawWithContent {
            // Draw actual content.
            drawContent()

            // Below is to draw the highlight, if necessary. A lot of the logic is copied from
            // Modifier.border
            val numCompositionsSinceTimeout =
                totalCompositions[0] - totalCompositionsAtLastTimeout.value

            val hasValidBorderParams = size.minDimension > 0f
            if (!hasValidBorderParams || numCompositionsSinceTimeout <= 0) {
                return@onDrawWithContent
            }

            val (color, strokeWidthPx) = when (numCompositionsSinceTimeout) {
                // We need at least one composition to draw, so draw the smallest border
                // color in blue.
                1L -> initialColor to 1f
                // 2 compositions is _probably_ okay.
                2L -> okColor to 2.dp.toPx()
                // 3 or more compositions before timeout may indicate an issue. lerp the
                // color from yellow to red, and continually increase the border size.
                else -> {
                    lerp(
                        warningColor.copy(alpha = 0.8f),
                        errorColor.copy(alpha = 0.5f),
                        min(1f, (numCompositionsSinceTimeout - 1).toFloat() / 100f)
                    ) to numCompositionsSinceTimeout.toInt().dp.toPx()
                }
            }

            val halfStroke = strokeWidthPx / 2
            val topLeft = Offset(halfStroke, halfStroke)
            val borderSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)

            val fillArea = (strokeWidthPx * 2) > size.minDimension
            val rectTopLeft = if (fillArea) Offset.Zero else topLeft
            val size = if (fillArea) size else borderSize
            val style = if (fillArea) Fill else Stroke(strokeWidthPx)

            drawRect(
                brush = SolidColor(color),
                topLeft = rectTopLeft,
                size = size,
                style = style
            )
        }
    }
}