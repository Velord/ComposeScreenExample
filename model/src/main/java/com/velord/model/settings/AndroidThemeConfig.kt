package com.velord.model.settings

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

data class AndroidThemeConfig(
    val config: ThemeConfig,
    val isSystemOsSwitchAvailable: Boolean,
    val isSystemDynamicColorAvailable: Boolean
) {
    companion object {

        val DEFAULT: AndroidThemeConfig = AndroidThemeConfig(
            config = ThemeConfig.DEFAULT,
            isSystemOsSwitchAvailable = isSystemOsSwitchAvailable(),
            isSystemDynamicColorAvailable = isSystemDynamicColorAvailable()
        )

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
        fun isSystemOsSwitchAvailable(): Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
        fun isSystemDynamicColorAvailable(): Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


        operator fun invoke(config: ThemeConfig): AndroidThemeConfig = AndroidThemeConfig(
            config = config,
            isSystemOsSwitchAvailable = isSystemOsSwitchAvailable(),
            isSystemDynamicColorAvailable = isSystemDynamicColorAvailable()
        )
    }
}