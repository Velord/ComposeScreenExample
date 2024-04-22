package com.velord.sharedviewmodel

import android.os.Build
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

//@KoinViewModel
class ThemeViewModel(
    private val getThemeConfigUC: GetThemeConfigUC
): CoroutineScopeViewModel() {

    val themeFlow = MutableStateFlow<Theme?>(null)

    init {
        launch {
            getThemeConfigUC.getConfigFlow().map {
                Theme(
                    config = it,
                    isSystemDynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                )
            }.collect {
                themeFlow.value = it
            }
        }
    }

    private fun saveNewTheme(newTheme: Theme) = launch {
        getThemeConfigUC.saveConfig(newTheme.config)
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