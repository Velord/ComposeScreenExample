package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

class SwitchThemeConfigUC(private val dataSource: GetThemeConfigDS) {
    suspend fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(
            useDarkTheme = config.useDarkTheme.not(),
            current = ThemeConfig.getOppositeTheme(config.current),
        )
        dataSource.save(newConfig)
    }
}