package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

/**
 * Toggles the `abideToOs` flag on the supplied [ThemeConfig] and persists the updated state.
 *
 * The argument is treated as the current snapshot, not as a partial update.
 */
fun interface SwitchAbideToOsThemeConfigUC : suspend (ThemeConfig) -> Unit
