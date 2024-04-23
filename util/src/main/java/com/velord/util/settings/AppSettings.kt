package com.velord.util.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val isAppFirstLaunch: Boolean,
    val theme: ThemeConfig
) {
    companion object {
        val DEFAULT = AppSettings(
            isAppFirstLaunch = false,
            theme = ThemeConfig.DEFAULT
        )
    }
}
