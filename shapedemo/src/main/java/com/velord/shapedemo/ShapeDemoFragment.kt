package com.velord.shapedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.velord.uicore.compose.path.createTicketPath
import com.velord.uicore.compose.theme.TicketShape
import com.velord.uicore.utils.setContentWithTheme

class ShapeDemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        ShapeDemoScreen()
    }
}

@Composable
private fun ShapeDemoScreen() {
    Content()
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        TicketShapeDemo()
    }
}

@Composable
private fun TicketShapeDemo() {
    Box(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceTint)
        ,
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
                modifier = Modifier.defaultMinSize(minWidth = 100.dp, minHeight = 80.dp)
            )
            Ticket(
                innerStrokeColor = MaterialTheme.colorScheme.primary,
                cornerRadius = 64.dp.value,
                modifier = Modifier.size(width = 200.dp, height = 100.dp),
                intervals = floatArrayOf(20f, 30f, 10f, 15f),
                innerStrokeScale = 0.75f,
                innerStrokeThickness = 8.dp.value,
            )
        }
    }
}

@Composable
private fun Ticket(
    innerStrokeColor: Color,
    cornerRadius: Float,
    modifier: Modifier = Modifier,
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .drawBehind {
                scale(scale = innerStrokeScale) {
                    drawPath(
                        path = createTicketPath(
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

@Preview
@Composable
private fun ShapeDemoPreview() {
    Content()
}