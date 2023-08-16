package com.example.widgetnewimage

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

private fun getImageKey(size: DpSize) = getImageKey(size.width.value, size.height.value)

private fun getImageKey(width: Float, height: Float) = stringPreferencesKey(
    "size-w:$width-h:$height",
)

private fun getImageProvider(path: String): ImageProvider {
    if (path.startsWith("content://")) ImageProvider(path.toUri())

    val bitmap = BitmapFactory.decodeFile(path)
    return ImageProvider(bitmap)
}

@Composable
internal fun NewImageWidgetScreen() {
    val prefs = currentState<Preferences>()
    val count = prefs[countPreferenceKey] ?: 0

    val size = LocalSize.current
    val imageKey = getImageKey(size)
    val filePath = prefs[imageKey] ?: ""
    Log.d("NewImageWidget", "Screen: id - $glanceId; C - $count; Path - $filePath")

    GlanceTheme {
        Content(
            count = count,
            filePath = filePath,
        )
    }
}

@Composable
private fun Content(
    count: Int,
    filePath: String,
) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = "Counter And Image Widget",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Counter(count)
        RefreshableImage(filePath)

        item {
            val size = LocalSize.current
            Text(
                text = "Current Widget Size:\n${size.width} x ${size.height}",
                modifier = GlanceModifier.padding(16.dp),
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    color = GlanceTheme.colors.onSecondaryContainer,
                    fontSize = 14.sp,
                ),
            )
        }
    }
}

private fun LazyListScope.Counter(count: Int) {
    item {
        Text(
            text = "Count: $count",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = GlanceTheme.colors.onSurfaceVariant,
                fontSize = 16.sp,
            ),
        )
    }

    item {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                text = "+",
                modifier = GlanceModifier
                    .width(100.dp)
                    .background(GlanceTheme.colors.surfaceVariant)
                    .padding(8.dp),
                onClick = actionRunCallback<UpdateCountCallback>(
                    parameters = actionParametersOf(
                        countWidgetKey to count + 1
                    )
                )
            )
            Button(
                text = "-",
                modifier = GlanceModifier
                    .width(100.dp)
                    .background(GlanceTheme.colors.surfaceVariant)
                    .padding(8.dp),
                onClick = actionRunCallback<UpdateCountCallback>(
                    parameters = actionParametersOf(
                        countWidgetKey to count - 1
                    )
                )
            )

        }
    }
}

private fun LazyListScope.RefreshableImage(filePath: String) {
    item {
        Button(
            text = LocalContext.current.getString(R.string.refresh),
            onClick = actionRunCallback<RefreshCallback>(
                parameters = actionParametersOf(
                    refreshWidgetKey to "RefreshCallback"
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
                provider = getImageProvider(filePath),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(8.dp)
            )
        } else {
            CircularProgressIndicator()
            val glanceId = LocalGlanceId.current
            SideEffect {

            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        count = 0,
        filePath = "",
    )
}