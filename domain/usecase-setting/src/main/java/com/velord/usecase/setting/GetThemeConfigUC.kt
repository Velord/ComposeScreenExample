package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

interface GetThemeDS {
    suspend fun getConfig(): ThemeConfig
    suspend fun saveConfig(config: ThemeConfig)
}

interface GetThemeConfigUC {
    suspend fun getConfig(): ThemeConfig
    suspend fun saveConfig(config: ThemeConfig)
}

class GetThemeConfigUCImpl(
    private val dataSource: GetThemeDS
) : GetThemeConfigUC {

    override suspend fun getConfig(): ThemeConfig = dataSource.getConfig()

    override suspend fun saveConfig(config: ThemeConfig) {
        dataSource.saveConfig(config)
    }
}