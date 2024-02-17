package com.velord.composescreenexample.ui.main

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.sharedviewmodel.CoroutineScopeViewModel
import com.velord.backend.service.auth.AuthService
import com.velord.datastore.DataStoreService
import com.velord.refreshableimage.RefreshableImageWidget
import com.velord.uicore.compose.glance.GlanceWidgetThemeSustainer
import com.velord.uicore.compose.glance.updateAll
import com.velord.util.settings.ThemeConfig
import com.velord.widgetcounter.CounterWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val authService: AuthService,
    private val dataStoreService: DataStoreService,
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