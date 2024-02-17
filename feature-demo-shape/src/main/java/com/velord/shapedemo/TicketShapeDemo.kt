package com.velord.shapedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.path.ticketPath
import com.velord.uicore.compose.theme.TicketShape

@Composable
internal fun TicketShapeDemo() {
    Title(text = "Ticket shape demo")

    Text (
        text = "",
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        )
    )

    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Ticket(
                innerStrokeColor = MaterialTheme.colorScheme.error,
                cornerRadius = 32.dp.value,
                modifier = Modifier.defaultMinSize(minWidth = 120.dp, minHeight = 80.dp)
            )
            Ticket(
                innerStrokeColor = MaterialTheme.colorScheme.primary,
                cornerRadius = 64.dp.value,
                modifier = Modifier.size(width = 200.dp, height = 100.dp),
                background = MaterialTheme.colorScheme.onSecondary,
                intervals = floatArrayOf(20f, 30f, 10f, 15f),
                innerStrokeScale = 0.75f,
                innerStrokeThickness = 8.dp.value,
            )
        }

        Ticket(
            innerStrokeColor = MaterialTheme.colorScheme.tertiary,
            cornerRadius = 66.dp.value,
            modifier = Modifier
                .padding(16.dp)
                .height(100.dp)
                .fillMaxWidth(),
            background = MaterialTheme.colorScheme.surface,
            intervals = floatArrayOf(2f, 30f, 10f, 15f),
            innerStrokeScale = 0.92f,
            innerStrokeThickness = 15.dp.value,
        )
    }
}

@Composable
private fun Ticket(
    innerStrokeColor: Color,
    cornerRadius: Float,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    intervals: FloatArray = floatArrayOf(10f, 20f),
    innerStrokeScale: Float = 0.85f,
    innerStrokeThickness: Float = 4.dp.value,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                shadowElevation = 8.dp.value
                shape = TicketShape(cornerRadius)
                clip = true
            }
            .background(background)
            .drawBehind {
                scale(scale = innerStrokeScale) {
                    drawPath(
                        path = ticketPath(
                            size = size,
                            cornerRadius = cornerRadius,
                        ),
                        color = innerStrokeColor,
                        style = Stroke(
                            width = innerStrokeThickness,
                            pathEffect = PathEffect.dashPathEffect(intervals, 0f),
                        )
                    )
                }
            }
        ,
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = "This is ticket",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}