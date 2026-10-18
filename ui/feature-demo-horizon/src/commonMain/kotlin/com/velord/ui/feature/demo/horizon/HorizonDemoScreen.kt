package com.velord.ui.feature.demo.horizon

import androidx.compose.runtime.Composable
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.horizonbloom.demo.HorizonDemoApp

@Composable
fun HorizonDemoScreen(onBackClick: () -> Unit) {
    HorizonDemoApp(onBackClick = onBackClick)
}

@PreviewCombined
@Composable
private fun Preview() {
    HorizonDemoScreen(onBackClick = {})
}
