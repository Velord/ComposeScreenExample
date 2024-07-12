package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface GetThemeConfigDS {
    suspend fun get(): Flow<ThemeConfig>
    suspend fun save(config: ThemeConfig)
}