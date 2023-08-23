package com.velord.uicore.compose.glance

import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceTheme
import androidx.glance.currentState

@Composable
fun MainGlanceTheme(
    widget: GlanceWidgetThemeSustainer<*>,
    content: @Composable () -> Unit
) {
    val prefs = currentState<Preferences>()
    val isDarkTheme = prefs[widget.useDarkThemePreferenceKey] ?: true
    val colors = if (isDarkTheme) {
        GlanceColorProviders.dark()
    } else {
        GlanceColorProviders.light()
    }
    GlanceTheme(colors) {
        content()
    }
}