package com.velord.gateway.setting

import com.velord.datastore.DataStoreService
import com.velord.usecase.setting.GetThemeDS
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class GetThemeGateway(
    private val dataStore: DataStoreService
) : GetThemeDS {

    override suspend fun getConfig(): ThemeConfig = dataStore.getThemeConfigFlow().first()

    override suspend fun saveConfig(config: ThemeConfig) {
       dataStore.setThemeConfig(config)
    }
}