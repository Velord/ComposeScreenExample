package com.velord.composescreenexample.ui.main

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.widgetnewimage.RefreshableImageWidget
import com.velord.backend.service.auth.AuthService
import com.velord.datastore.DataStoreService
import com.velord.uicore.compose.glance.GlanceWidgetThemeSustainer
import com.velord.uicore.compose.glance.updateAll
import com.velord.util.viewModel.BaseViewModel
import com.velord.util.viewModel.ThemeSwitcher
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
) : BaseViewModel() {

    private val widgets = listOf<GlanceWidgetThemeSustainer<*>>(
        RefreshableImageWidget()
    )

    fun updateTheme(themeSwitcher: ThemeSwitcher?) {
        if (themeSwitcher == null) return

        updateAllWidgets(themeSwitcher)
        updateDataStore()
    }

    private fun updateAllWidgets(themeSwitcher: ThemeSwitcher) = launch {
        widgets.updateAll(context, themeSwitcher)
    }

    private fun updateDataStore() {
        // TODO: save theme to data store
    }
}