package com.velord.uicore.utils

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {

    val themeSwitcherFlow = MutableStateFlow(ThemeSwitcher.DEFAULT)

    fun changeTheme(newThemeSwitcher: ThemeSwitcher) = launch {
        themeSwitcherFlow.emit(newThemeSwitcher)
    }
}