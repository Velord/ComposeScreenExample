package com.velord.model.setting

enum class ThemeMode {
    LIGHT, DARK, BOTH;

    fun isDark(currentUseDarkTheme: Boolean): Boolean = when(this) {
        DARK -> true
        LIGHT -> false
        BOTH -> currentUseDarkTheme
    }
}

enum class SpecialTheme(
    val mode: ThemeMode,
    val isDefault: Boolean = false
) {
    LIGHT(mode = ThemeMode.LIGHT, isDefault = true),
    DARK(mode = ThemeMode.DARK, isDefault = true),
    NEGATIVE_LIGHT(mode = ThemeMode.DARK),
    OCEAN(mode = ThemeMode.BOTH);

    fun isEnabled(useDynamicColor: Boolean): Boolean = (useDynamicColor && isDefault.not()).not()

    fun getOppositeDefaultTheme(newUseDark: Boolean): SpecialTheme {
        val newMode = if (newUseDark) ThemeMode.DARK else ThemeMode.LIGHT
        if (this.mode == newMode || this.mode == ThemeMode.BOTH) return this

        return if (newUseDark) DEFAULT_DARK_SCHEME else DEFAULT_LIGHT_SCHEME
    }

    companion object {
        val DEFAULT_DARK_SCHEME: SpecialTheme = DARK
        val DEFAULT_LIGHT_SCHEME: SpecialTheme = LIGHT

        fun getAvailableThemeRoster(useDarkTheme: Boolean): List<SpecialTheme> {
            val targetMode = if (useDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT
            return entries.filter { it.mode == targetMode || it.mode == ThemeMode.BOTH }
        }
    }
}