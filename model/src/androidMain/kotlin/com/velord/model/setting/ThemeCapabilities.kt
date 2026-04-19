package com.velord.model.setting

import android.os.Build

internal actual val currentThemeCapabilities: ThemeCapabilities = ThemeCapabilities(
    isSystemOsSwitchAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1,
    isSystemDynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
)