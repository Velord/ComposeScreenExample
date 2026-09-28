package com.velord.usecase.setting

import com.velord.model.setting.SpecialTheme
import com.velord.model.setting.ThemeConfig

/**
 * Updates the theme configuration with a specific [SpecialTheme] and persists the updated state.
 */
fun interface SwitchSpecialThemeConfigUC : suspend (ThemeConfig, SpecialTheme) -> Unit
