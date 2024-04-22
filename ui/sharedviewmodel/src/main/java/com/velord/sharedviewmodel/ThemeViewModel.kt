package com.velord.sharedviewmodel

import android.os.Build
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.util.settings.AndroidThemeConfig
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val getThemeConfigUC: GetThemeConfigUC
): CoroutineScopeViewModel() {

    val themeFlow = MutableStateFlow<AndroidThemeConfig?>(null)

    init {
        launch {
            getThemeConfigUC.getConfigFlow().map {
                AndroidThemeConfig(
                    config = it,
                    isSystemDynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                )
            }.collect {
                themeFlow.value = it
            }
        }
    }

    private fun saveNewTheme(newTheme: AndroidThemeConfig) = launch {
        getThemeConfigUC.saveConfig(newTheme.config)
    }

    fun changeSystemTheme(currentTheme: AndroidThemeConfig) = launch {
        val newConfig = ThemeConfig(
            useDarkTheme = currentTheme.config.useDarkTheme,
            useDynamicColor = currentTheme.config.useDynamicColor.not()
        )
        saveNewTheme(currentTheme.copy(config = newConfig))
    }

    fun changeDarkTheme(currentTheme: AndroidThemeConfig) = launch {
        val newConfig = ThemeConfig(
            useDarkTheme = currentTheme.config.useDarkTheme.not(),
            useDynamicColor = currentTheme.config.useDynamicColor
        )
        saveNewTheme(currentTheme.copy(config = newConfig))
    }
}