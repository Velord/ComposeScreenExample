package com.velord.ui.widget.refreshableimage

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import co.touchlab.kermit.Logger
import com.velord.ui.widget.refreshableimage.model.ImageParameter

internal class RefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newParameter: ImageParameter = requireNotNull(
            parameters[RefreshableImageWidget.refreshableImageWidgetKey]
        ) {
            "Missing refreshableImageWidgetKey"
        }
        log.d { "RefreshCallback.onAction: $glanceId; Size: $newParameter" }

        val manager = GlanceAppWidgetManager(context)
        manager.getGlanceIds(RefreshableImageWidget::class.java).forEach {
            updateAppWidgetState(context, it) { prefs ->
                prefs[RefreshableImageWidget.isDownloadingNewImagePreferenceKey] = true
            }
        }
        RefreshableImageWidget().update(context, glanceId)
        RefreshableImageWidgetWorker.enqueue(context, glanceId, newParameter, force = true)
    }

    companion object {
        private val log = Logger.withTag("RefreshCallback")
    }
}
