package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined

@Composable
internal fun BoxScope.HeaderShroud() {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.34f),
                        Color.Black.copy(alpha = 0.24f),
                    ),
                )
            )
            .statusBarsPadding()
            .height(50.dp)
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(132.dp)
            .background(Color.Gray),
    ) {
        HeaderShroud()
    }
}
