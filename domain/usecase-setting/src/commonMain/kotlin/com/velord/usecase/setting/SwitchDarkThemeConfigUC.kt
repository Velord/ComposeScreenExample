package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig

fun interface SwitchDarkThemeConfigUC : suspend (ThemeConfig) -> Unit
