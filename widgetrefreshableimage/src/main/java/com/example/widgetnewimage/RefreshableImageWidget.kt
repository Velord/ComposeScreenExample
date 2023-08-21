package com.example.widgetnewimage

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.DpSize
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

internal val refreshImageWidgetKey = ActionParameters.Key<DpSize>("refreshImageWidgetKey")

class RefreshableImageWidget : GlanceAppWidget(errorUiLayout = R.layout.refreshable_image_widget_error_layout) {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { NewImageWidgetScreen() }
}

internal class RefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newSize: DpSize = requireNotNull(parameters[refreshImageWidgetKey]) {
            "Missing refreshWidgetKey"
        }
        Log.d("RefreshableImageWidget", "RefreshCallback.onAction: $glanceId; Size: $newSize")

        RefreshableImageWidgetWorker.enqueu(context, newSize, glanceId, force = true)
    }
}

class RefreshableWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RefreshableImageWidget()
}