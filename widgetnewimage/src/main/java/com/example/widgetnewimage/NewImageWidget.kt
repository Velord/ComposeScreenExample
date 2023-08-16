package com.example.widgetnewimage

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

internal val refreshWidgetKey = ActionParameters.Key<String>("refreshNewImageWidgetKey")
internal val refreshPreferenceKey = stringPreferencesKey("refreshNewImagePreferenceKey")

internal val countWidgetKey = ActionParameters.Key<Int>("countNewImageWidgetKey")
internal val countPreferenceKey = intPreferencesKey("countNewImagePreferenceKey")

class NewImageWidget : GlanceAppWidget(errorUiLayout = R.layout.new_image_widget_error_layout) {

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

//    override val sizeMode: SizeMode = SizeMode.Responsive(
//        setOf(SMALL_SQUARE, HORIZONTAL_RECTANGLE, BIG_SQUARE)
//    )

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("NewImageWidget", "provideGlance: $id")
        provideContent { NewImageWidgetScreen(id) }
    }
}

internal class RefreshCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("NewImageWidget", "RefreshCallback.onAction: $glanceId")
    }
}

internal class UpdateCountCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("NewImageWidget", "UpdateCountCallback.onAction: $glanceId")

        val newCount = requireNotNull(parameters[countWidgetKey]) {
            "Missing countWidgetKey"
        }

        updateAppWidgetState(context, glanceId) {
            it.apply {
                this[countPreferenceKey] = newCount
            }
        }

        Log.d("NewImageWidget", "UpdateCountCallback.onAction: $glanceId; newCount=$newCount")
        NewImageWidget().updateAll(context)
    }
}

class NewImageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NewImageWidget()
}