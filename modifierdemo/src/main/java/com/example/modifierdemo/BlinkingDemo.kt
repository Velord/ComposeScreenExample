package com.example.modifierdemo

import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.uicore.utils.modifier.blinkingShadow

@Composable
internal fun BlinkingShadowDemo() {
    SmallLeft(text = "Modifier.blinkingShadow")

    GreenReverse()
    RedReverse()
    CyanRestart()
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun GreenReverse() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 32.dp)
            .padding(horizontal = 32.dp)
            .height(100.dp)
            .blinkingShadow(
                elevationMax = 64.dp,
                shape = CardDefaults.shape,
                duration = 3000,
                spotColor = Color.Green
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Green Reverse", Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun RedReverse() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .blinkingShadow(
                elevationMax = 16.dp,
                shape = CardDefaults.shape,
                duration = 500,
                spotColor = Color.Red
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Red Reverse", Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun CyanRestart() {
    val shape = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(70.dp)
            .blinkingShadow(
                elevationMax = 32.dp,
                shape = shape,
                spotColor = Color.Cyan,
                repeatMode = RepeatMode.Restart
            ),
        shape = shape,
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Cyan Restart", Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
private fun BlinkingShadowDemoPreview() {
    BlinkingShadowDemo()
}