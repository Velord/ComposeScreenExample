package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

fun interface SwitchAbideToOsThemeConfigUC : suspend (ThemeConfig) -> Unit

class SwitchAbideToOsThemeConfigUCImpl(
    private val dataSource: GetThemeConfigDS
) : SwitchAbideToOsThemeConfigUC {

    override suspend operator fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(abideToOs = config.abideToOs.not())
        dataSource.save(newConfig)
    }
}
