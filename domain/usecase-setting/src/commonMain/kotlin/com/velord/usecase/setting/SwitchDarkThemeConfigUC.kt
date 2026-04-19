package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

/**
 * Toggles dark-theme usage on the supplied [ThemeConfig] and persists the updated state.
 *
 * Implementations are also expected to update the concrete current theme so it stays consistent
 * with the new dark/light choice.
 */
fun interface SwitchDarkThemeConfigUC : suspend (ThemeConfig) -> Unit
