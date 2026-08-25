package com.velord.ui.sharedviewmodel

import com.velord.model.setting.AppShapeStyle
import com.velord.model.setting.AppThemeConfig
import com.velord.model.setting.SpecialTheme
import com.velord.model.setting.ThemeConfig
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDarkThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import com.velord.usecase.setting.SwitchShapeStyleThemeConfigUC
import com.velord.usecase.setting.SwitchSpecialThemeConfigUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Why is it null ? Cause we need to wait for the first value to be emitted from datastore.
// Also theme is not only available inside the app. It is also available in the widgets.
// This means we should reduce unnecessary calls for switching theme.
// Default AndroidThemeConfig is acceptable only inside the app(Compose).
data class ThemeUiState(val appThemeConfig: AppThemeConfig?) {

    companion object {
        val DEFAULT = ThemeUiState(null)
    }
}

sealed interface ThemeUiAction {
    data object AbideToOsThemeSwitch : ThemeUiAction
    data object DynamicThemeSwitch : ThemeUiAction
    data object DarkThemeSwitch : ThemeUiAction
    data class SpecialThemeSwitch(val theme: SpecialTheme) : ThemeUiAction
    data class ShapeStyleSwitch(val style: AppShapeStyle) : ThemeUiAction
}

class ThemeVM(
    private val getThemeConfigUC: GetThemeConfigUC,
    private val switchDynamicColorThemeConfigUC: SwitchDynamicColorThemeConfigUC,
    private val switchAbideToOsThemeConfigUC: SwitchAbideToOsThemeConfigUC,
    private val switchDarkThemeConfigUC: SwitchDarkThemeConfigUC,
    private val switchSpecialThemeConfigUC: SwitchSpecialThemeConfigUC,
    private val switchShapeStyleThemeConfigUC: SwitchShapeStyleThemeConfigUC,
) : CoroutineScopeVM() {

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

    private fun onAbideToOsThemeSwitch() {
        val config = uiStateFlow.value.appThemeConfig?.config ?: return
        launch { switchAbideToOsThemeConfigUC.invoke(config) }
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

    private fun onSpecialThemeSwitch(theme: SpecialTheme) {
        switchConfig {
            switchSpecialThemeConfigUC.invoke(it, theme)
        }
    }

    private fun onShapeStyleSwitch(style: AppShapeStyle) {
        val config = uiStateFlow.value.appThemeConfig?.config ?: return
        launch { switchShapeStyleThemeConfigUC.invoke(config, style) }
    }

    private fun switchConfig(f: suspend (ThemeConfig) -> Unit) {
        val config = uiStateFlow.value.appThemeConfig?.config ?: return
        if (config.abideToOs) return

        launch { f(config) }
    }

    private fun observe() {
        launch {
            getThemeConfigUC().map {
                val newAppThemeConfig: AppThemeConfig = uiStateFlow.value.appThemeConfig
                    ?.copy(config = it)
                    ?: AppThemeConfig.invoke(it)
                uiStateFlow.value.copy(appThemeConfig = newAppThemeConfig)
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
                    is ThemeUiAction.SpecialThemeSwitch -> onSpecialThemeSwitch(it.theme)
                    is ThemeUiAction.ShapeStyleSwitch -> onShapeStyleSwitch(it.style)
                }
            }
        }
    }
}
