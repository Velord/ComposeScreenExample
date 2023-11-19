package com.velord.util.settings

import kotlinx.serialization.Serializable

@Serializable
data class ThemeConfig(
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean
) {
    companion object {
        val DEFAULT = ThemeConfig(
            useDarkTheme = false,
            useDynamicColor = false
        )
    }
}