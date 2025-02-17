package com.velord.sharedviewmodel

import com.velord.model.settings.AndroidThemeConfig
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import com.velord.usecase.setting.SwitchThemeConfigUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val getThemeConfigUC: GetThemeConfigUC,
    private val switchDynamicColorThemeConfigUC: SwitchDynamicColorThemeConfigUC,
    private val switchAbideToOsThemeConfigUC: SwitchAbideToOsThemeConfigUC,
    private val switchThemeConfigUC: SwitchThemeConfigUC
): CoroutineScopeViewModel() {

    val themeFlow = MutableStateFlow<AndroidThemeConfig?>(null)

    init {
        launch {
            getThemeConfigUC().map {
                themeFlow.value?.copy(config = it) ?: AndroidThemeConfig.invoke(it)
            }.collect {
                themeFlow.value = it
            }
        }
    }

    fun onSwitchToOsTheme() = launch {
        themeFlow.value?.let {
            switchAbideToOsThemeConfigUC.invoke(it.config)
        }
    }

    fun onChangeDynamicTheme() = launch {
        themeFlow.value?.let {
            if (it.config.abideToOs) return@launch
            switchDynamicColorThemeConfigUC.invoke(it.config)
        }
    }

    fun onChangeDarkTheme() = launch {
        themeFlow.value?.let {
            if (it.config.abideToOs) return@launch
            switchThemeConfigUC.invoke(it.config)
        }
    }
}