package com.velord.util.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val isAppFirstLaunch: Boolean = false,
    val theme: ThemeConfig = ThemeConfig.DEFAULT
)
