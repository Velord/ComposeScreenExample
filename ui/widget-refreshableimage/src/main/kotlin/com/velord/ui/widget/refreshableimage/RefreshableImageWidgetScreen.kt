package com.velord.ui.widget.refreshableimage

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
import co.touchlab.kermit.Logger
import com.velord.core.resource.Res
import com.velord.core.resource.downloaded_from
import com.velord.core.resource.image_widget
import com.velord.core.resource.refresh
import com.velord.core.resource.widget_size
import com.velord.core.ui.compose.glance.MainGlanceTheme
import com.velord.ui.widget.refreshableimage.util.createImageParameter
import com.velord.ui.widget.refreshableimage.util.getImageFilePath
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

// On emulator redundant compositions with wrong LocalSize.current ruin all flow
private const val ERROR_COMPOSITION_WIDTH = 675

internal val log = Logger.withTag("RefreshableImageWidgetScreen")

@Composable
internal fun RefreshableImageWidgetScreen() {
    if (LocalSize.current.width.value.roundToInt() == ERROR_COMPOSITION_WIDTH) return

    val prefs = currentState<Preferences>()
    val imageParameter = prefs.createImageParameter(false)
    val filePath = prefs.getImageFilePath(imageParameter)
    val sourceUrl = RefreshableImageWidgetWorker.createUrl(imageParameter)
    val isDownloading = prefs[RefreshableImageWidget.isDownloadingNewImagePreferenceKey] ?: false

    log.d { "Screen: id - ${LocalGlanceId.current};\nPath - $filePath;\nUrl - $sourceUrl" }
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
        CurrentSize(
            url = url,
            isDownloadingNewImage = isDownloadingNewImage,
        )
        RefreshableImage(filePath)
    }
}

@Composable
private fun Title() {
    Text(
        text = stringResource(Res.string.image_widget),
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
            text = stringResource(Res.string.widget_size, size.width.value, size.height.value),
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 12.sp,
            ),
        )

        Refresh(
            url,
            isDownloadingNewImage,
        )
    }
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(Res.string.downloaded_from, url),
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
    log.d { "isDownloading: id - $isDownloading" }
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
                    RefreshableImageWidget.refreshableImageWidgetKey to
                        prefs.createImageParameter(true)
                )
            )),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.refresh),
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
        val imageParameter = currentState<Preferences>().createImageParameter(false)
        SideEffect {
            RefreshableImageWidgetWorker.enqueue(context, glanceId, imageParameter)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Content(
        filePath = "",
        url = "",
        isDownloadingNewImage = false,
    )
}
