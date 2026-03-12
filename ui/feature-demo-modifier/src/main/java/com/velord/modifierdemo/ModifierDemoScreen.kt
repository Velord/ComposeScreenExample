package com.velord.modifierdemo

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.modifierdemo.component.BlinkingShadowDemo
import com.velord.modifierdemo.component.HangingDemo
import com.velord.modifierdemo.component.ShimmeringDemo
import com.velord.modifierdemo.component.SwellingDemo

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

@Preview
@Composable
private fun ModifierDemoPreview() {
    Content()
}