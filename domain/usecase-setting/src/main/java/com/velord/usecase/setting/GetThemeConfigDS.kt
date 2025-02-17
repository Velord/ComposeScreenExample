package com.velord.usecase.setting

import com.velord.model.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface GetThemeConfigDS {
    suspend fun getFlow(): Flow<ThemeConfig>
    suspend fun save(config: ThemeConfig)
}