package com.velord.model.setting

import kotlinx.serialization.Serializable

@Serializable
data class AppSetting(
    val isAppFirstLaunch: Boolean,
    val theme: ThemeConfig
) {
    companion object {
        val DEFAULT = AppSetting(
            isAppFirstLaunch = false,
            theme = ThemeConfig.DEFAULT
        )
    }
}
