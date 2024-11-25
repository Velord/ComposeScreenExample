package com.velord.shapedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.uicore.utils.modifier.WaveSide
import com.velord.uicore.utils.modifier.waveShape

@Composable
internal fun WaveShapeDemo() {
    Title(text = "Wave shape demo")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .size(200.dp)
            .waveShape(period = 4.dp, amplitude = 24.dp)
            .background(Color.Red)
    ) {}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .size(100.dp)
            .waveShape(period = 24.dp, amplitude = 8.dp, side = WaveSide.Bottom)
            .background(Color.Blue)
    ) {}
}