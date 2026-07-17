package com.velord.infrastructure.di

import com.velord.model.setting.ThemeConfig

internal fun interface ThemeWidgetRefreshAction {
    suspend fun refresh(config: ThemeConfig)
}
