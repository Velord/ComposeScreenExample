package com.velord.ui.feature.demo.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.demo.modifier.component.BlinkingShadowDemo
import com.velord.ui.feature.demo.modifier.component.HangingDemo
import com.velord.ui.feature.demo.modifier.component.ShimmeringDemo
import com.velord.ui.feature.demo.modifier.component.SwellingDemo

@Composable
fun ModifierDemoScreen() {
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
        ShimmeringDemo()
        BlinkingShadowDemo()
        HangingDemo()
        SwellingDemo()
    }
}

@Composable
internal fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(top = 32.dp, start = 16.dp),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Content()
}
