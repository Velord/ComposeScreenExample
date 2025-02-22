package com.velord.sharedviewmodel

import com.velord.model.settings.AndroidThemeConfig
import com.velord.model.settings.ThemeConfig
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDarkThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Why is it null ? Cause we need to wait for the first value to be emitted from datastore.
// Also theme is not only available inside the app. It is also available in the widgets.
// This means we should reduce unnecessary calls for switching theme.
// Default AndroidThemeConfig is acceptable only inside the app(Compose).
data class ThemeUiState(
    val androidThemeConfig: AndroidThemeConfig?
) {
    companion object {
        val DEFAULT = ThemeUiState(null)
    }
}

sealed interface ThemeUiAction {
    data object AbideToOsThemeSwitch : ThemeUiAction
    data object DynamicThemeSwitch : ThemeUiAction
    data object DarkThemeSwitch : ThemeUiAction
}

class ThemeViewModel(
    private val getThemeConfigUC: GetThemeConfigUC,
    private val switchDynamicColorThemeConfigUC: SwitchDynamicColorThemeConfigUC,
    private val switchAbideToOsThemeConfigUC: SwitchAbideToOsThemeConfigUC,
    private val switchDarkThemeConfigUC: SwitchDarkThemeConfigUC
): CoroutineScopeViewModel() {

    val uiStateFlow = MutableStateFlow(ThemeUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<ThemeUiAction>()

    init {
        observe()
    }

    fun onAction(action: ThemeUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onAbideToOsThemeSwitch() = launch {
        uiStateFlow.value.androidThemeConfig?.let {
            switchAbideToOsThemeConfigUC.invoke(it.config)
        }
    }

    private fun onDynamicThemeSwitch() {
        switchConfig {
            switchDynamicColorThemeConfigUC.invoke(it)
        }
    }

    private fun onDarkThemeSwitch() {
        switchConfig {
            switchDarkThemeConfigUC.invoke(it)
        }
    }

    private fun switchConfig(f: suspend (ThemeConfig) -> Unit) {
        val config = uiStateFlow.value.androidThemeConfig?.config ?: return
        if (config.abideToOs) return

        launch { f(config) }
    }

    private fun observe() {
        launch {
            getThemeConfigUC().map {
                val newAndroidThemeConfig: AndroidThemeConfig = uiStateFlow.value.androidThemeConfig
                    ?.copy(config = it)
                    ?: AndroidThemeConfig.invoke(it)
                uiStateFlow.value.copy(androidThemeConfig = newAndroidThemeConfig)
            }.collect {
                uiStateFlow.value = it
            }
        }
        launch {
            actionFlow.collect {
                when (it) {
                    is ThemeUiAction.AbideToOsThemeSwitch -> onAbideToOsThemeSwitch()
                    is ThemeUiAction.DynamicThemeSwitch -> onDynamicThemeSwitch()
                    is ThemeUiAction.DarkThemeSwitch -> onDarkThemeSwitch()
                }
            }
        }
    }
}