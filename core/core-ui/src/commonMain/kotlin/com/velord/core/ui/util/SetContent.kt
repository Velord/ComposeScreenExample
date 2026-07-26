package com.velord.core.ui.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.velord.model.setting.AppThemeConfig

val LocalTheme = staticCompositionLocalOf { AppThemeConfig.DEFAULT }
