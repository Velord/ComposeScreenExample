package com.velord.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TestScreen(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Content(
        title = title,
        modifier = modifier,
        onClick = onClick,
    )
}

@Composable
private fun Content(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val time = remember { System.currentTimeMillis().toString() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            text = time,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface
        )

        Button(
            onClick = onClick,
            modifier = modifier.align(Alignment.BottomCenter),
        ) {
            Text(
                text = stringResource(title),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Content(title = com.velord.core.resource.R.string.settings)
}