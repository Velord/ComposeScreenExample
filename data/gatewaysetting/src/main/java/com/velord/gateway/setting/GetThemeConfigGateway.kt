package com.velord.gateway.setting

import com.velord.datastore.DataStoreService
import com.velord.usecase.setting.GetThemeConfigDS
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

// Lets imagine it is another data store in different module
interface AppState {
    val flow: MutableStateFlow<ThemeConfig>
}
@Single
class AppStateImpl : AppState {
    override val flow = MutableStateFlow(ThemeConfig.DEFAULT)
}

@Single
class GetThemeConfigGateway(
    private val dataStore: DataStoreService,
    private val appState: AppState
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