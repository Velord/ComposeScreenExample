package com.velord.gateway.setting

import com.velord.appstate.AppStateService
import com.velord.datastore.DataStoreService
import com.velord.usecase.setting.GetThemeConfigDS
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetThemeConfigGateway(
    private val dataStore: DataStoreService,
    private val appState: AppStateService
) : GetThemeConfigDS {

    private var isInitialized = false

    override suspend fun get(): ThemeConfig {
        if (isInitialized.not()) {
            isInitialized = true
            appState.flow.value = dataStore.getAppSettingsFlow().map { it.theme }.first()
        }

        return appState.flow.value
    }

    override suspend fun save(config: ThemeConfig) {
        dataStore.setThemeConfig(config)
        appState.flow.value = config
    }
}