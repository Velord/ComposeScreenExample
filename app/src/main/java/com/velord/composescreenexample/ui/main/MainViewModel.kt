package com.velord.composescreenexample.ui.main

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.velord.core.ui.compose.glance.GlanceWidgetThemeSustainer
import com.velord.core.ui.compose.glance.updateAll
import com.velord.model.settings.ThemeConfig
import com.velord.refreshableimage.RefreshableImageWidget
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.widgetcounter.CounterWidget
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : CoroutineScopeViewModel() {

    private val widgets = listOf<GlanceWidgetThemeSustainer<*>>(
        RefreshableImageWidget(), CounterWidget()
    )

    fun updateTheme(themeConfig: ThemeConfig?) {
        if (themeConfig == null) return

        updateAllWidgets(themeConfig)
        updateDataStore()
    }

    private fun updateAllWidgets(themeConfig: ThemeConfig) = launch {
        widgets.updateAll(context, themeConfig)
    }

    private fun updateDataStore() {
        // TODO: save theme to data store
    }
}