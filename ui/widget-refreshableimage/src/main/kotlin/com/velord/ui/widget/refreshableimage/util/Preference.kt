package com.velord.ui.widget.refreshableimage.util

import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.glance.LocalSize
import com.velord.ui.widget.refreshableimage.RefreshableImageWidget
import com.velord.ui.widget.refreshableimage.log
import com.velord.ui.widget.refreshableimage.model.ImageParameter

@Composable
internal fun Preferences.createImageParameter(generateNewSeed: Boolean): ImageParameter {
    val size = LocalSize.current
    val seed = if (generateNewSeed) {
        randomStringByKotlinRandom()
    } else {
        this[RefreshableImageWidget.seedPreferenceKey] ?: ImageParameter.DEFAULT_SEED
    }

    return ImageParameter(seed, size)
}

internal fun Preferences.getImageFilePath(imageParameter: ImageParameter): String {
    val imageKey = RefreshableImageWidget.getImageUriKey(imageParameter)
    log.d { "Screen: seed - ${imageParameter.seed}; UriKey - $imageKey" }
    return this[imageKey] ?: ""
}
