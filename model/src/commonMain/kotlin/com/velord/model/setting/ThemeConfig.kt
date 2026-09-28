package com.velord.model.setting

import kotlinx.serialization.Serializable

enum class AppShapeStyle {
    ROUNDED, CUT, SQUARE
}

@Serializable
data class ThemeConfig(
    val abideToOs: Boolean,
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean,
    val current: SpecialTheme,
    val shapeStyle: AppShapeStyle,
) {
    companion object {
        val DEFAULT = ThemeConfig(
            abideToOs = true,
            useDarkTheme = false,
            useDynamicColor = false,
            current = SpecialTheme.LIGHT,
            shapeStyle = AppShapeStyle.ROUNDED
        )

        fun invoke(
            useDarkTheme: Boolean,
            useDynamicColor: Boolean
        ): ThemeConfig {
            val theme = findDefaultTheme(useDarkTheme)
            return ThemeConfig(
                abideToOs = true,
                useDarkTheme = useDarkTheme,
                useDynamicColor = useDynamicColor,
                current = theme,
                shapeStyle = AppShapeStyle.ROUNDED
            )
        }

        private fun findDefaultTheme(isDark: Boolean): SpecialTheme {
            val targetMode = if (isDark) ThemeMode.DARK else ThemeMode.LIGHT
            return SpecialTheme.entries
                .filter { it.isDefault }
                .find { it.mode == targetMode || it.mode == ThemeMode.BOTH }
                ?: error("No default theme found")
        }
    }
}
