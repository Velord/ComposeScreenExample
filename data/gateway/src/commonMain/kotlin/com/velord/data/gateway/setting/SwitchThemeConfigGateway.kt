package com.velord.data.gateway.setting

import com.velord.model.setting.AppShapeStyle
import com.velord.model.setting.SpecialTheme
import com.velord.model.setting.ThemeConfig
import org.koin.core.annotation.Single

@Single
class SwitchThemeConfigGateway(private val getThemeConfigGateway: GetThemeConfigGateway) {

    suspend fun switchAbideToOs(config: ThemeConfig) {
        val newConfig = config.copy(abideToOs = config.abideToOs.not())
        getThemeConfigGateway.save(newConfig)
    }

    suspend fun switchDynamicColor(config: ThemeConfig) {
        val newConfig = config.copy(useDynamicColor = config.useDynamicColor.not())
        getThemeConfigGateway.save(newConfig)
    }

    suspend fun switchSpecialTheme(config: ThemeConfig, newTheme: SpecialTheme) {
        val newConfig = config.copy(
            useDarkTheme = newTheme.mode.isDark(config.useDarkTheme),
            current = newTheme,
        )
        getThemeConfigGateway.save(newConfig)
    }

    suspend fun switchDarkTheme(config: ThemeConfig) {
        val newUseDark = config.useDarkTheme.not()
        val newConfig = config.copy(
            useDarkTheme = newUseDark,
            current = config.current.getOppositeDefaultTheme(newUseDark),
        )
        getThemeConfigGateway.save(newConfig)
    }

    suspend fun switchShapeStyle(config: ThemeConfig, newStyle: AppShapeStyle) {
        val newConfig = config.copy(shapeStyle = newStyle)
        getThemeConfigGateway.save(newConfig)
    }
}
