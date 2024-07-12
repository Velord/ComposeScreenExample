package com.velord.util.settings

import kotlinx.serialization.Serializable

enum class SpecialTheme(
    val isDark: Boolean,
    val default: Boolean
) {
    LIGHT(false, true),
    DARK(true, true);
}

@Serializable
data class ThemeConfig(
    val abideToOs: Boolean,
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean,
    val current: SpecialTheme
) {
    companion object {

        val DEFAULT = ThemeConfig(
            abideToOs = true,
            useDarkTheme = false,
            useDynamicColor = false,
            current = SpecialTheme.LIGHT
        )

        fun getOppositeTheme(theme: SpecialTheme): SpecialTheme = when (theme) {
            SpecialTheme.LIGHT -> SpecialTheme.DARK
            SpecialTheme.DARK -> SpecialTheme.LIGHT
        }

        fun invoke(
            useDarkTheme: Boolean,
            useDynamicColor: Boolean
        ): ThemeConfig {
            val theme = findDefaultTheme(useDarkTheme)
            return ThemeConfig(
                abideToOs = true,
                useDarkTheme = useDarkTheme,
                useDynamicColor = useDynamicColor,
                current = theme
            )
        }

        private fun findDefaultTheme(isDark: Boolean): SpecialTheme =
            SpecialTheme.entries
                .find { it.isDark == isDark && it.default }
                ?: error("No default theme found")
    }
}