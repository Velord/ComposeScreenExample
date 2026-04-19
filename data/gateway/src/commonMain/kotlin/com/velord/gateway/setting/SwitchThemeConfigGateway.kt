package com.velord.gateway.setting

import com.velord.model.setting.ThemeConfig
import org.koin.core.annotation.Single

@Single
class SwitchThemeConfigGateway(private val themeConfigSaver: ThemeConfigSaver) {

    suspend fun switchAbideToOs(config: ThemeConfig) {
        val newConfig = config.copy(abideToOs = config.abideToOs.not())
        themeConfigSaver.save(newConfig)
    }

    suspend fun switchDynamicColor(config: ThemeConfig) {
        val newConfig = config.copy(useDynamicColor = config.useDynamicColor.not())
        themeConfigSaver.save(newConfig)
    }

    suspend fun switchDarkTheme(config: ThemeConfig) {
        val newConfig = config.copy(
            useDarkTheme = config.useDarkTheme.not(),
            current = ThemeConfig.getOppositeTheme(config.current),
        )
        themeConfigSaver.save(newConfig)
    }
}
