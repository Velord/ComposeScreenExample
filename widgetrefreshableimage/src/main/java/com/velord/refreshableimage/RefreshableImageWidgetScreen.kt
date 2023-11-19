package com.velord.refreshableimage

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
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
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import com.velord.uicore.compose.glance.MainGlanceTheme
import kotlin.math.roundToInt

// On emulator redundant compositions with wrong LocalSize.current ruin all flow
private const val ERROR_COMPOSITION_WIDTH = 675

@Composable
private fun Preferences.createImageParameters(generateNewSeed: Boolean): ImageParameters {
    val size = LocalSize.current
    val seed = if (generateNewSeed) {
        randomStringByKotlinRandom()
    } else {
        this[RefreshableImageWidget.seedPreferenceKey] ?: ImageParameters.DEFAULT_SEED
    }
    return ImageParameters(seed, size)
}

private fun Preferences.getImageFilePath(parameters: ImageParameters): String {
    val imageKey = RefreshableImageWidget.getImageUriKey(parameters)
    Log.d("RefreshableImageWidget", "Screen: seed - ${parameters.seed}; UriKey - $imageKey")
    return this[imageKey] ?: ""
}

@Composable
internal fun NewImageWidgetScreen() {
    if (LocalSize.current.width.value.roundToInt() == ERROR_COMPOSITION_WIDTH) return

    val prefs = currentState<Preferences>()
    val parameters = prefs.createImageParameters(false)
    val filePath = prefs.getImageFilePath(parameters)
    val sourceUrl = RefreshableImageWidgetWorker.createUrl(parameters)
    val isDownloading = prefs[RefreshableImageWidget.isDownloadingNewImagePreferenceKey] ?: false

    Log.d("RefreshableImageWidget", "Screen: id - ${LocalGlanceId.current};\nPath - $filePath;\nUrl - $sourceUrl")
    MainGlanceTheme(RefreshableImageWidget()) {
        Content(
            filePath = filePath,
            url = sourceUrl,
            isDownloadingNewImage = isDownloading,
        )
    }
}

@Composable
private fun Content(
    filePath: String,
    url: String,
    isDownloadingNewImage: Boolean
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title()
        CurrentSize(url, isDownloadingNewImage)
        RefreshableImage(filePath)
    }
}

@Composable
private fun Title() {
    Text(
        text = LocalContext.current.getString(R.string.image_widget),
        modifier = GlanceModifier.padding(top = 16.dp),
        style = TextStyle(
            color = GlanceTheme.colors.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
    )
}

@Composable
private fun CurrentSize(
    url: String,
    isDownloadingNewImage: Boolean
) {
    val size = LocalSize.current

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = LocalContext.current.getString(R.string.widget_size, size.width.value, size.height.value),
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 12.sp,
            ),
        )

        Refresh(url, isDownloadingNewImage)
    }
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = LocalContext.current.getString(R.string.downloaded_from, url),
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = GlanceTheme.colors.onBackground,
                fontSize = 12.sp,
            ),
        )
    }
}

@Composable
private fun Refresh(url: String, isDownloadingNewImage: Boolean) {
    val isDownloading = if (url.isEmpty()) true else isDownloadingNewImage
    Log.d("RefreshableImageWidget", "isDownloading: id - $isDownloading")
    val prefs = currentState<Preferences>()
    Row(
        modifier = GlanceModifier
            .height(48.dp)
            .fillMaxWidth()
            .background(GlanceTheme.colors.secondaryContainer)
            .cornerRadius(16.dp)
            .clickable(
                actionRunCallback<RefreshCallback>(
                parameters = actionParametersOf(
                    RefreshableImageWidget.refreshableImageWidgetKey to prefs.createImageParameters(true)
                )
            )),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = LocalContext.current.getString(R.string.refresh),
            modifier = GlanceModifier.padding(horizontal = 8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onSecondaryContainer,
                fontSize = 14.sp,
            ),
        )

        if (isDownloading) {
            CircularProgressIndicator(
                modifier = GlanceModifier,
                color = GlanceTheme.colors.onSecondaryContainer,
            )
        }
    }
}

@Composable
private fun RefreshableImage(filePath: String) {
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
        CircularProgressIndicator(
            modifier = GlanceModifier.padding(24.dp),
            color = GlanceTheme.colors.onSurface,
        )

        val context = LocalContext.current
        val glanceId = LocalGlanceId.current
        val parameters = currentState<Preferences>().createImageParameters(false)
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
        url = "",
        isDownloadingNewImage = false,
    )
}