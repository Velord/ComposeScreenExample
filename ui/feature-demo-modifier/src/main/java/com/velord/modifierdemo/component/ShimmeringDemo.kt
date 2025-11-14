package com.velord.modifierdemo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.utils.modifier.shimmering
import com.velord.modifierdemo.Title

private fun Modifier.default(): Modifier = this
    .fillMaxWidth()
    .height(100.dp)
    .padding(top = 10.dp)

@Composable
internal fun ShimmeringDemo() {
    Title(text = "Modifier.shimmering")

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
                gradientColorAndPosition = { animatedValue ->
                    listOf(
                        Color.Red to 0f,
                        Color.Green to animatedValue * 0.1f,
                        Color.Blue to animatedValue * 0.2f,
                        Color.Yellow to animatedValue * 0.3f,
                        Color.Magenta to animatedValue * 0.7f,
                        Color.Cyan to animatedValue * 0.8f,
                        Color.Gray to animatedValue * 0.9f,
                        Color.Black to animatedValue,
                        Color.White to 1f,
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
                gradientColorAndPosition = { animatedValue ->
                    listOf(
                        Color.Magenta to 0f,
                        Color.Cyan to animatedValue * 0.3f,
                        Color.Gray to animatedValue,
                        Color.Magenta to 1f,
                    ) }

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
                gradientColorAndPosition = { animatedValue ->
                    listOf(
                        MaterialTheme.colorScheme.surfaceTint to 0f,
                        MaterialTheme.colorScheme.tertiary to animatedValue,
                        MaterialTheme.colorScheme.surfaceTint to 1f,
                    )
                }
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
                gradientColorAndPosition = { animatedValue ->
                    listOf(
                        MaterialTheme.colorScheme.tertiaryContainer to 0f,
                        MaterialTheme.colorScheme.onTertiaryContainer to animatedValue,
                        MaterialTheme.colorScheme.tertiaryContainer to 1f,
                    )
                },
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
                gradientColorAndPosition = { animatedValue ->
                    listOf(
                        Color.Green to 0f,
                        Color.Yellow to animatedValue * 0.1f,
                        Color.DarkGray to animatedValue * 0.2f,
                        Color.Magenta to animatedValue * 0.3f,
                        Color.Cyan to animatedValue * 0.35f,
                        Color.Transparent to animatedValue * 0.7f,
                        Color.Gray to animatedValue * 0.8f,
                        Color.Black to animatedValue * 0.85f,
                        Color.White to animatedValue * 0.9f,
                        Color.LightGray to animatedValue * 0.95f,
                        Color.Red to animatedValue,
                        Color.Blue to 1f
                    )
                },
                reverse = true
            ),
        textAlign = TextAlign.Center
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Column {
        ShimmeringDemo()
    }
}