package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

/**
 * Toggles dynamic-color usage on the supplied [ThemeConfig] and persists the updated state.
 *
 * The argument is treated as the current full snapshot.
 */
fun interface SwitchDynamicColorThemeConfigUC : suspend (ThemeConfig) -> Unit
