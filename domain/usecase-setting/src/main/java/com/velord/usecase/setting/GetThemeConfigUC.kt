package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

interface GetThemeConfigDS {
    suspend fun get(): ThemeConfig
    suspend fun save(config: ThemeConfig)
}

class GetThemeConfigUC(
    private val dataSource: GetThemeConfigDS
) {

    suspend fun getConfig(): ThemeConfig = dataSource.get()

    suspend fun saveConfig(config: ThemeConfig) {
        dataSource.save(config)
    }
}