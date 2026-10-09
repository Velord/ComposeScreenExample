package com.velord.data.gateway.setting

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.datastore.DataStoreDataSource
import com.velord.model.setting.AppThemeConfig
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetThemeConfigGateway(
    private val dataStore: DataStoreDataSource,
    private val appState: AppStateDataSource,
) {

    private var isInitialized = false

    suspend fun getFlow(): Flow<ThemeConfig> {
        if (isInitialized.not()) {
            isInitialized = true
            runCatching {
                val config = dataStore
                    .getAppSettingFlow()
                    .map { it.theme }
                    .first()
                val isDynamicColorEnabled = config.useDynamicColor
                val isDynamicColorAvailable = AppThemeConfig.DEFAULT.isSystemDynamicColorAvailable

                // Restored preferences may request dynamic colors on a device without support.
                when {
                    isDynamicColorEnabled && isDynamicColorAvailable.not() -> save(
                        config.copy(useDynamicColor = false)
                    )
                    else -> appState.themeConfigFlow.value = config
                }
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
