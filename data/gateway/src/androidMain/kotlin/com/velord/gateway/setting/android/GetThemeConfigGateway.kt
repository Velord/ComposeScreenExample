package com.velord.gateway.setting.android

import com.velord.appstate.AppStateDataSource
import com.velord.datastore.DataStoreDataSource
import com.velord.gateway.setting.ThemeConfigSaver
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [ThemeConfigSaver::class])
class GetThemeConfigGateway(
    private val dataStore: DataStoreDataSource,
    private val appState: AppStateDataSource,
) : ThemeConfigSaver {

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

    override suspend fun save(config: ThemeConfig) {
        runCatching {
            dataStore.setThemeConfig(config)
        }
        appState.themeConfigFlow.value = config
    }
}
