package com.example.widgetnewimage

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
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
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlin.random.Random

private const val STRING_LENGTH = 6
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
private fun randomStringByKotlinRandom() = (1..STRING_LENGTH)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")


private fun createImageParameters(size: DpSize, tag: String, generateNewSeed: Boolean): ImageParameters {
    val seed = if (generateNewSeed) {
        randomStringByKotlinRandom()
    } else {
        ImageParameters.DEFAULT_SEED
    }
    Log.d("RefreshableImageWidget", "tag: $tag; newSeed = $seed; force = $generateNewSeed")
    return ImageParameters(seed, size)
}

@Composable
private fun Preferences.getImageFilePath(): String {
    val size = LocalSize.current
    val seed = this[RefreshableImageWidget.seedPreferenceKey] ?: ImageParameters.DEFAULT_SEED
    val parameters = ImageParameters(seed, size)
    val imageKey = RefreshableImageWidget.getImageUriKey(parameters)
    Log.d("RefreshableImageWidget", "Screen: seed - ${seed}; UriKey - $imageKey")
    return this[imageKey] ?: ""
}

@Composable
internal fun NewImageWidgetScreen() {
    val prefs = currentState<Preferences>()

    val filePath = prefs.getImageFilePath()
    val sourceUrl = prefs[RefreshableImageWidget.sourceUrlPreferenceKey] ?: ""

    Log.d("RefreshableImageWidget", "Screen: id - ${LocalGlanceId.current};\nPath - $filePath;\nUrl - $sourceUrl")

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
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title()
        CurrentSize(url)
        RefreshableImage(filePath)
    }
}

@Composable
private fun Title() {
    Text(
        text = "Image Widget",
        modifier = GlanceModifier.padding(16.dp),
        style = TextStyle(
            color = ColorProvider(MaterialTheme.colorScheme.onSurface),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
    )
}

@Composable
private fun CurrentSize(url: String) {
    val size = LocalSize.current

    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Widget Size:\nW: ${size.width.value} x H: ${size.height.value}",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                color = ColorProvider(MaterialTheme.colorScheme.onSecondaryContainer),
                fontSize = 14.sp,
            ),
        )
    }
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Downloaded from:\n$url",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = ColorProvider(MaterialTheme.colorScheme.onSecondaryContainer),
                fontSize = 12.sp,
            ),
        )
    }
}

@Composable
private fun RefreshableImage(filePath: String) {
    val size = LocalSize.current
    Button(
        text = LocalContext.current.getString(R.string.refresh),
        onClick = actionRunCallback<RefreshCallback>(
                parameters = actionParametersOf(
                    RefreshableImageWidget.refreshableImageWidgetKey to createImageParameters(size,"refresh",true)
                )
            )
        ,
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer),
    )

    if (filePath.isNotEmpty()) {
        Image(
            provider = RefreshableImageWidget.getImageProvider(LocalContext.current, filePath),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .cornerRadius(8.dp)
        )
    } else {
        CircularProgressIndicator()

        val context = LocalContext.current
        val glanceId = LocalGlanceId.current
        val parameters = createImageParameters(size,"when empty",false)
        SideEffect {
            RefreshableImageWidgetWorker.enqueue(context, glanceId, parameters)
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