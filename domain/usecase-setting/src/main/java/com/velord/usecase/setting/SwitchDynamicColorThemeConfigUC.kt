package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

fun interface SwitchDynamicColorThemeConfigUC : suspend (ThemeConfig) -> Unit

class SwitchDynamicColorThemeConfigUCImpl(
    private val dataSource: GetThemeConfigDS
) : SwitchDynamicColorThemeConfigUC {

    override suspend operator fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(useDynamicColor = config.useDynamicColor.not())
        dataSource.save(newConfig)
    }
}
