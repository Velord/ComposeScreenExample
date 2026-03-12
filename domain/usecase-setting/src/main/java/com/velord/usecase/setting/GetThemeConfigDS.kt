package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface GetThemeConfigDS {
    suspend fun getFlow(): Flow<ThemeConfig>
    suspend fun save(config: ThemeConfig)
}