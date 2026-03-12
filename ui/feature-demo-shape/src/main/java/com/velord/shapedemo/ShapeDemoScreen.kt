package com.velord.shapedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.shapedemo.component.PervasiveArcFromBottomShapeDemo
import com.velord.shapedemo.component.TicketShapeDemo
import com.velord.shapedemo.component.WaveShapeDemo

@Composable
<<<<<<<< HEAD:feature-demo-shape/src/main/java/com/velord/shapedemo/ShapeDemoFragment.kt
internal fun ShapeDemoScreen() {
========
fun ShapeDemoScreen() {
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:ui/feature-demo-shape/src/main/java/com/velord/shapedemo/ShapeDemoScreen.kt
    Content()
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        TicketShapeDemo()
        PervasiveArcFromBottomShapeDemo()
        WaveShapeDemo()

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Preview
@Composable
private fun ShapeDemoPreview() {
    Content()
}