package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

fun interface SwitchDarkThemeConfigUC : suspend (ThemeConfig) -> Unit

class SwitchDarkThemeConfigUCImpl(
    private val dataSource: GetThemeConfigDS
) : SwitchDarkThemeConfigUC {

    override suspend operator fun invoke(config: ThemeConfig) {
        val newConfig = config.copy(
            useDarkTheme = config.useDarkTheme.not(),
            current = ThemeConfig.getOppositeTheme(config.current),
        )
        dataSource.save(newConfig)
    }
}
