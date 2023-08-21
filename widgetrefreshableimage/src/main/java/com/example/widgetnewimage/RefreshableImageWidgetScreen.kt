package com.example.widgetnewimage

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

@Composable
private fun createParametersSize(): ParametersSize {
    val size = LocalSize.current
    return ParametersSize(size.width.value, size.height.value)
}

@Composable
internal fun NewImageWidgetScreen() {
    val prefs = currentState<Preferences>()

    val size = LocalSize.current
    val imageKey = RefreshableImageWidget.getImageUriKey(size)
    val filePath = prefs[imageKey] ?: ""

    val sourceUrl = prefs[RefreshableImageWidget.sourceUrlKey] ?: ""

    Log.d("RefreshableImageWidget", "Screen: id - ${LocalGlanceId.current}; Path - $filePath; Url - $sourceUrl")

    GlanceTheme {
        Content(
            filePath = filePath,
            url = sourceUrl
        )
    }
}

@Composable
private fun Content(
    filePath: String,
    url: String
) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title()
        CurrentSize(url)
        RefreshableImage(filePath)
    }
}

private fun LazyListScope.Title() {
    item {
        Text(
            text = "Image Widget",
            modifier = GlanceModifier.padding(16.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
        )
    }
}

private fun LazyListScope.CurrentSize(url: String) {
    item {
        val size = LocalSize.current
        Text(
            text = "Widget Size:\nW: ${size.width} x H: ${size.height}",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onSecondaryContainer,
                fontSize = 14.sp,
            ),
        )
    }

    item {
        Text(
            text = "Downloaded from:\n$url",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = GlanceTheme.colors.onSecondaryContainer,
                fontSize = 12.sp,
            ),
        )
    }
}

private fun LazyListScope.RefreshableImage(filePath: String) {
    item {
        Button(
            text = LocalContext.current.getString(R.string.refresh),
            onClick = actionRunCallback<RefreshCallback>(
                parameters = actionParametersOf(
                    RefreshableImageWidget.refreshableImageWidgetKey to createParametersSize()
                )
            ),
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(GlanceTheme.colors.secondaryContainer),
        )
    }

    item {
        if (filePath.isNotEmpty()) {
            Image(
                provider = RefreshableImageWidget.getImageProvider(filePath),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        } else {
            CircularProgressIndicator()

            val context = LocalContext.current
            val glanceId = LocalGlanceId.current
            val size = createParametersSize()
            SideEffect {
                RefreshableImageWidgetWorker.enqueue(context, glanceId, size)
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        filePath = "",
        url = ""
    )
}