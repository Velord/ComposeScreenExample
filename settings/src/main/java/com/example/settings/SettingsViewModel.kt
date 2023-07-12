package com.example.settings

import com.velord.uicore.utils.ThemeSwitcher
import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {
    val themeSwitcherFlow = MutableSharedFlow<ThemeSwitcher>()

    fun changeTheme(newThemeSwitcher: ThemeSwitcher) = launch {
        themeSwitcherFlow.emit(newThemeSwitcher)
    }
}