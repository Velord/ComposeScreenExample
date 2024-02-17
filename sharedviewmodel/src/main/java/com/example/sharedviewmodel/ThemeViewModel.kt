package com.example.sharedviewmodel

import android.os.Build
import com.velord.datastore.DataStoreService
import com.velord.util.settings.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Theme(
    val config: ThemeConfig,
    val isSystemDynamicColorAvailable: Boolean
) {
    companion object {
        val DEFAULT: Theme = Theme(
            config = ThemeConfig.DEFAULT,
            isSystemDynamicColorAvailable = false
        )
    }
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStoreService
): CoroutineScopeViewModel() {

    val themeFlow = MutableStateFlow<Theme?>(null)

    init {
        launch {
            themeFlow.value = Theme(
                config = dataStore.getThemeConfig(),
                isSystemDynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            )
        }
    }

    private fun saveNewTheme(newTheme: Theme) = launch {
        dataStore.setThemeConfig(newTheme.config)
        themeFlow.emit(newTheme)
    }

    fun changeSystemTheme(currentTheme: Theme) = launch {
        val newConfig = ThemeConfig(
            useDarkTheme = currentTheme.config.useDarkTheme,
            useDynamicColor = currentTheme.config.useDynamicColor.not()
        )
        saveNewTheme(currentTheme.copy(config = newConfig))
    }

    fun changeDarkTheme(currentTheme: Theme) = launch {
        val newConfig = ThemeConfig(
            useDarkTheme = currentTheme.config.useDarkTheme.not(),
            useDynamicColor = currentTheme.config.useDynamicColor
        )
        saveNewTheme(currentTheme.copy(config = newConfig))
    }
}