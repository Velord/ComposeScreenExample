package com.velord.ui.feature.demo.shape

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
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.demo.shape.component.PervasiveArcFromBottomShapeDemo
import com.velord.ui.feature.demo.shape.component.TicketShapeDemo
import com.velord.ui.feature.demo.shape.component.WaveShapeDemo

@Composable
fun ShapeDemoScreen() {
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

@PreviewCombined
@Composable
private fun Preview() {
    Content()
}
