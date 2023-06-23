package com.example.modifierdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velord.uicore.utils.shimmering

private fun Modifier.default(): Modifier = this
    .fillMaxWidth()
    .height(100.dp)
    .padding(top = 10.dp)

@Composable
internal fun ShimmeringDemo() {
    Rainbow()
    Default()
    Magenta()
    SurfaceTint()
    Reverse()
    ReverseRainbow()
}

@Composable
private fun Rainbow() {
    Text(
        text = "Rainbow",
        modifier = Modifier
            .default()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp))
            .shimmering(
                gradientColors = listOf(
                    Color.Red,
                    Color.Green,
                    Color.Blue,
                    Color.Yellow,
                    Color.Magenta,
                    Color.Cyan,
                    Color.Gray,
                    Color.Black,
                    Color.White,
                ),
                colorsPosition = { animatedValue ->
                    listOf(
                        0f,
                        animatedValue * 0.1f,
                        animatedValue * 0.2f,
                        animatedValue * 0.3f,
                        animatedValue * 0.7f,
                        animatedValue * 0.8f,
                        animatedValue * 0.9f,
                        animatedValue,
                        1f
                    )
                }
            )
        ,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Default() {
    Text(
        text = "Default",
        modifier = Modifier
            .default()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .shimmering(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Magenta() {
    Text(
        text = "Magenta",
        modifier = Modifier
            .default()
            .padding(horizontal = 4.dp)
            .clip(CutCornerShape(16.dp))
            .shimmering(
                duration = 2000,
                gradientColors = listOf(Color.Magenta, Color.Cyan, Color.Gray, Color.Magenta),
                colorsPosition = { animatedValue ->
                    listOf(0f, animatedValue * 0.3f, animatedValue, 1f)
                }
            ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SurfaceTint() {
    Text(
        text = "Surface Tint",
        modifier = Modifier
            .default()
            .padding(horizontal = 24.dp)
            .clip(CutCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceTint)
            .shimmering(
                duration = 3000,
                gradientColors = listOf(
                    MaterialTheme.colorScheme.surfaceTint,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.surfaceTint,
                ),
                colorsPosition = { animatedValue -> listOf(0f, animatedValue, 1f) }
            ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Reverse() {
    Text(
        text = "Reverse",
        modifier = Modifier
            .default()
            .padding(horizontal = 8.dp)
            .clip(
                AbsoluteCutCornerShape(
                    topLeft = 4.dp,
                    topRight = 4.dp,
                    bottomLeft = 32.dp,
                    bottomRight = 32.dp
                )
            )
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .shimmering(
                duration = 3000,
                gradientColors = listOf(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.onTertiaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer,
                ),
                colorsPosition = { animatedValue -> listOf(0f, animatedValue, 1f) },
                reverse = true
            ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReverseRainbow() {
    Text(
        text = "Reverse Rainbow",
        modifier = Modifier
            .default()
            .padding(horizontal = 16.dp)
            .clip(
                AbsoluteRoundedCornerShape(
                    topLeft = 4.dp,
                    topRight = 8.dp,
                    bottomLeft = 16.dp,
                    bottomRight = 24.dp
                )
            )
            .shimmering(
                duration = 3000,
                gradientColors = listOf(
                    Color.Green,
                    Color.Yellow,
                    Color.DarkGray,
                    Color.Magenta,
                    Color.Cyan,
                    Color.Transparent,
                    Color.Gray,
                    Color.Black,
                    Color.White,
                    Color.LightGray,
                    Color.Red,
                    Color.Blue,
                ),
                colorsPosition = { animatedValue ->
                    listOf(
                        0f,
                        animatedValue * 0.1f,
                        animatedValue * 0.2f,
                        animatedValue * 0.3f,
                        animatedValue * 0.35f,
                        animatedValue * 0.7f,
                        animatedValue * 0.8f,
                        animatedValue * 0.85f,
                        animatedValue * 0.9f,
                        animatedValue * 0.95f,
                        animatedValue,
                        1f
                    )
                },
                reverse = true
            ),
        textAlign = TextAlign.Center
    )
}