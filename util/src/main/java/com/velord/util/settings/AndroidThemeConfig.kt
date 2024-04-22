package com.velord.util.settings

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

data class AndroidThemeConfig(
    val config: ThemeConfig,
    val isSystemDynamicColorAvailable: Boolean
) {
    companion object {
        val DEFAULT: AndroidThemeConfig = AndroidThemeConfig(
            config = ThemeConfig.DEFAULT,
            isSystemDynamicColorAvailable = isSystemDynamicColorAvailable()
        )

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
        fun isSystemDynamicColorAvailable(): Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}