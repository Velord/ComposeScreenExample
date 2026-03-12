package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

class SwitchDarkThemeConfigUC(private val dataSource: GetThemeConfigDS) {

    suspend operator fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(
            useDarkTheme = config.useDarkTheme.not(),
            current = ThemeConfig.getOppositeTheme(config.current),
        )
        dataSource.save(newConfig)
    }
}