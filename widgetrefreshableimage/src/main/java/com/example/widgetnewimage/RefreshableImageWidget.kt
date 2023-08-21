package com.example.widgetnewimage

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import kotlinx.parcelize.Parcelize

@Parcelize
class ParametersSize(
    val width: Float,
    val height: Float
) : Parcelable {
    override fun toString(): String = "Width = $width x Height=$height"
}

internal val refreshImageWidgetKey = ActionParameters.Key<ParametersSize>("refreshImageWidgetKey")

class RefreshableImageWidget : GlanceAppWidget(errorUiLayout = R.layout.refreshable_image_widget_error_layout) {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { NewImageWidgetScreen() }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        //RefreshableImageWidgetWorker.cancel(context, glanceId)
    }
}

internal class RefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newSize: ParametersSize = requireNotNull(parameters[refreshImageWidgetKey]) {
            Log.d("RefreshableImageWidget", "Missing refreshWidgetKey")
            "Missing refreshWidgetKey"
        }
        Log.d("RefreshableImageWidget", "RefreshCallback.onAction: $glanceId; Size: $newSize")

        RefreshableImageWidgetWorker.enqueu(context, glanceId, newSize, force = true)
    }
}

class RefreshableWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RefreshableImageWidget()
}