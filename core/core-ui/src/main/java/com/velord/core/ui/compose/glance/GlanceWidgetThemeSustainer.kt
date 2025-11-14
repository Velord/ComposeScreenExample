package com.velord.core.ui.compose.glance

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.velord.model.setting.ThemeConfig

interface GlanceWidgetThemeSustainer <T : GlanceAppWidget> {
    val name: Class<T>
    val useDarkThemePreferenceKey: Preferences.Key<Boolean>
}

suspend fun List<GlanceWidgetThemeSustainer<*>>.updateAll(
    context: Context,
    themeConfig: ThemeConfig
) {
    val manager = GlanceAppWidgetManager(context)
    this.forEach { kClassGlanceWidget ->
        manager.getGlanceIds(kClassGlanceWidget.name).forEach {
            updateAppWidgetState(context, it) { prefs ->
                prefs[kClassGlanceWidget.useDarkThemePreferenceKey] = themeConfig.useDarkTheme
            }
        }
        (kClassGlanceWidget as GlanceAppWidget).updateAll(context)
    }
}