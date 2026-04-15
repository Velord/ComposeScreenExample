package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow

fun interface GetThemeConfigUC : suspend () -> Flow<ThemeConfig>
