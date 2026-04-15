package com.velord.gateway.setting

import com.velord.appstate.AppStateDataSource
import com.velord.datastore.DataStoreDataSource
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetThemeConfigGateway(
    private val dataStore: DataStoreDataSource,
    private val appState: AppStateDataSource
) {

    private var isInitialized = false

    suspend fun getFlow(): Flow<ThemeConfig> {
        if (isInitialized.not()) {
            isInitialized = true
            runCatching {
                appState.themeConfigFlow.value = dataStore.getAppSettingFlow().map { it.theme }.first()
            }
        }

        return appState.themeConfigFlow
    }

    suspend fun save(config: ThemeConfig) {
        runCatching {
            dataStore.setThemeConfig(config)
        }
        appState.themeConfigFlow.value = config
    }
}
