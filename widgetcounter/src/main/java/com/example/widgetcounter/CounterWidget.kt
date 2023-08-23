package com.example.widgetcounter

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
import com.velord.uicore.compose.glance.GlanceWidgetThemeSustainer

class CounterWidget :
    GlanceAppWidget(errorUiLayout = R.layout.counter_widget_error_layout),
    GlanceWidgetThemeSustainer<CounterWidget> {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode = SizeMode.Exact

    override val name: Class<CounterWidget> = CounterWidget::class.java
    override val useDarkThemePreferenceKey: Preferences.Key<Boolean> = CounterWidget.useDarkThemePreferenceKey

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { CounterWidgetScreen() }

    companion object {
        internal val actionParameterKey = ActionParameters.Key<Int>("countWidgetKey")

        internal val preferenceKey = intPreferencesKey("countPreferenceKey")
        internal val useDarkThemePreferenceKey = booleanPreferencesKey("use_dark_theme")
    }
}

internal class UpdateCountCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newCount = requireNotNull(parameters[CounterWidget.actionParameterKey]) {
            "Missing countWidgetKey"
        }

        updateAppWidgetState(context, glanceId) {
            it.apply {
                this[CounterWidget.preferenceKey] = newCount
            }
        }

        CounterWidget().update(context, glanceId)
    }
}

class CounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CounterWidget()
}