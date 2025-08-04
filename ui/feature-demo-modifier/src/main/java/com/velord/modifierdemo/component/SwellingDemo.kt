package com.velord.modifierdemo.component

import androidx.compose.animation.core.EaseInBounce
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.modifierdemo.Title
import com.velord.uicore.utils.modifier.swelling

@Composable
internal fun ColumnScope.SwellingDemo() {
    Title(text = "Modifier.swelling")
    Default()
    OneAndAHalf()
    HalfToOnePointTwo()
    TitleWithBounce()
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
private fun ColumnScope.Default() {
    Icon(
        imageVector = Icons.Filled.PointOfSale,
        contentDescription = null,
        modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterHorizontally)
            .swelling(),
        tint = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun OneAndAHalf() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 32.dp)
            .swelling(targetScale = 1.5f),
    ) {
        Text(
            text = "1.5X",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HalfToOnePointTwo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .swelling(
                initialScale = 0.5f,
                targetScale = 1.2f,
                duration = 2000
            )
    ) {
        Text(
            text = "0.5x to 1.2x in 2s",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun TitleWithBounce() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .swelling(
                initialScale = 0.7f,
                targetScale = 1.1f,
                duration = 3000,
                easing = EaseInBounce
            )
    ) {
        Text(
            text = "0.7x to 1.1x in 3s with bouncing",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
private fun SwellingDemoPreview() {
    Column {
        SwellingDemo()
    }
}