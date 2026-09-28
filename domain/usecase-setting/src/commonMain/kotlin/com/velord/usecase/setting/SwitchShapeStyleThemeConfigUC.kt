package com.velord.usecase.setting

import com.velord.model.setting.AppShapeStyle
import com.velord.model.setting.ThemeConfig

fun interface SwitchShapeStyleThemeConfigUC : suspend (ThemeConfig, AppShapeStyle) -> Unit
