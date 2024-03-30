package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

interface GetThemeConfigDS {
    suspend fun get(): ThemeConfig
    suspend fun save(config: ThemeConfig)
}

interface GetThemeConfigUC {
    suspend fun getConfig(): ThemeConfig
    suspend fun saveConfig(config: ThemeConfig)
}

class GetThemeConfigUCImpl(
    private val dataSource: GetThemeConfigDS
) : GetThemeConfigUC {

    override suspend fun getConfig(): ThemeConfig = dataSource.get()

    override suspend fun saveConfig(config: ThemeConfig) {
        dataSource.save(config)
    }
}