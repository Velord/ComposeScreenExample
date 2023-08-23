package com.velord.uicore.compose.glance

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.velord.util.viewModel.ThemeSwitcher

interface GlanceWidgetThemeSustainer <T : GlanceAppWidget> {
    val name: Class<T>
    val useDarkThemePreferenceKey: Preferences.Key<Boolean>
}

suspend fun List<GlanceWidgetThemeSustainer<*>>.updateAll(
    context: Context,
    themeSwitcher: ThemeSwitcher
) {
    val manager = GlanceAppWidgetManager(context)
    this.forEach { kClassGlanceWidget ->
        manager.getGlanceIds(kClassGlanceWidget.name).forEach {
            updateAppWidgetState(context, it) { prefs ->
                prefs[kClassGlanceWidget.useDarkThemePreferenceKey] = themeSwitcher.useDarkTheme
            }
        }
        (kClassGlanceWidget as GlanceAppWidget).updateAll(context)
    }
}