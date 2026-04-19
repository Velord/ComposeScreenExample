package com.velord.model.setting

import kotlinx.serialization.Serializable

@Serializable
data class AppThemeConfig(
    val config: ThemeConfig,
    val isSystemOsSwitchAvailable: Boolean,
    val isSystemDynamicColorAvailable: Boolean
) {
    companion object {

        operator fun invoke(config: ThemeConfig): AppThemeConfig = AppThemeConfig(
            config = config,
            isSystemOsSwitchAvailable = currentThemeCapabilities.isSystemOsSwitchAvailable,
            isSystemDynamicColorAvailable = currentThemeCapabilities.isSystemDynamicColorAvailable
        )

        val DEFAULT = AppThemeConfig(ThemeConfig.DEFAULT)
    }
}