package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface GetThemeConfigDS {
    suspend fun get(): Flow<ThemeConfig>
    suspend fun save(config: ThemeConfig)
}

class GetThemeConfigUC(
    private val dataSource: GetThemeConfigDS
) {

    suspend fun getConfigFlow(): Flow<ThemeConfig> = dataSource.get()

    suspend fun saveConfig(config: ThemeConfig) {
        dataSource.save(config)
    }
}