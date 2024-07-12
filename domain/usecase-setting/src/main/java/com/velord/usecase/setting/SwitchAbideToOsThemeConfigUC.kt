package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

class SwitchAbideToOsThemeConfigUC(private val dataSource: GetThemeConfigDS) {
    suspend fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(abideToOs = config.abideToOs.not())
        dataSource.save(newConfig)
    }
}