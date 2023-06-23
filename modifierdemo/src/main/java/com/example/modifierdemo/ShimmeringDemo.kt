package com.example.modifierdemo

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.uicore.utils.shimmering

@Composable
fun ColumnScope.ShimmeringDemo() {
    repeat(5) {
        Text(
            text = "Hello World",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
        )
    }
}