package com.example.widgetcounter

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

internal val countWidgetKey = ActionParameters.Key<Int>("countWidgetKey")
internal val countPreferenceKey = intPreferencesKey("countPreferenceKey")

class CounterWidget : GlanceAppWidget(errorUiLayout = R.layout.counter_widget_error_layout) {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { CounterWidgetScreen() }
}

internal class UpdateCountCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newCount = requireNotNull(parameters[countWidgetKey]) {
            "Missing countWidgetKey"
        }

        updateAppWidgetState(context, glanceId) {
            it.apply {
                this[countPreferenceKey] = newCount
            }
        }

        CounterWidget().update(context, glanceId)
    }
}

class CounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CounterWidget()
}