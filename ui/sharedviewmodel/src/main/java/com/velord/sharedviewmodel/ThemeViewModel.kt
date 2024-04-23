package com.velord.sharedviewmodel

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
                AndroidThemeConfig.invoke(it)
            }.collect {
                themeFlow.value = it
            }
        }
    }

    private fun saveNewTheme(newTheme: AndroidThemeConfig) = launch {
        getThemeConfigUC.saveConfig(newTheme.config)
    }

    fun onSwitchToOsTheme() = launch {
        themeFlow.value?.let {
            val newConfig = it.config.copy(abideToOs = it.config.abideToOs.not())
            saveNewTheme(it.copy(config = newConfig))
        }
    }

    fun onChangeDynamicTheme() = launch {
        themeFlow.value?.let {
            if (it.config.abideToOs) return@launch

            val newConfig = it.config.copy(
                useDynamicColor = it.config.useDynamicColor.not()
            )
            saveNewTheme(it.copy(config = newConfig))
        }
    }

    fun onChangeDarkTheme() = launch {
        themeFlow.value?.let {
            if (it.config.abideToOs) return@launch

            val newConfig = it.config.copy(
                useDarkTheme = it.config.useDarkTheme.not(),
                current = ThemeConfig.getOppositeTheme(it.config.current),
            )
            saveNewTheme(it.copy(config = newConfig))
        }
    }
}