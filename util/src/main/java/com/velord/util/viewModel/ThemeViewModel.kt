package com.velord.util.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class ThemeSwitcher(
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean
) {
    companion object {
        val DEFAULT = ThemeSwitcher(
            useDarkTheme = false,
            useDynamicColor = false
        )
    }
}

class ThemeViewModel : BaseViewModel() {

    val themeSwitcherFlow = MutableStateFlow<ThemeSwitcher?>(null)

    fun changeSystemTheme(currentSwitcher: ThemeSwitcher) {
        launch {
            val newSwitcher = ThemeSwitcher(
                useDarkTheme = currentSwitcher.useDarkTheme,
                useDynamicColor = currentSwitcher.useDynamicColor.not()
            )
            themeSwitcherFlow.emit(newSwitcher)
        }
    }

    fun changeDarkTheme(currentSwitcher: ThemeSwitcher) {
        launch {
            val newSwitcher = ThemeSwitcher(
                useDarkTheme = currentSwitcher.useDarkTheme.not(),
                useDynamicColor = currentSwitcher.useDynamicColor
            )
            themeSwitcherFlow.emit(newSwitcher)
        }
    }
}