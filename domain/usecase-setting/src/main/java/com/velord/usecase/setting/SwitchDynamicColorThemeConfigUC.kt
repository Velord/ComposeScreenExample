package com.velord.usecase.setting

import com.velord.model.settings.ThemeConfig

class SwitchDynamicColorThemeConfigUC(private val dataSource: GetThemeConfigDS) {
    suspend fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(useDynamicColor = config.useDynamicColor.not())
        dataSource.save(newConfig)
    }
}