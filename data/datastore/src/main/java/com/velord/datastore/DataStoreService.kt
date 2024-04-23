package com.velord.datastore

import com.velord.datastore.appSettings.AppSettingsDataStore
import com.velord.util.settings.AppSettings
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

interface DataStoreService {
    suspend fun checkAppFirstLaunch(): Boolean
    suspend fun setThemeConfig(theme: ThemeConfig)
    suspend fun getAppSettingsFlow(): Flow<AppSettings>
}

@Single
class DataStoreServiceImpl(
    private val settings: AppSettingsDataStore
) : DataStoreService {

    private suspend fun setFirstLaunch() {
        settings.updateData {
            it.copy(isAppFirstLaunch = true)
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = settings.data.map {
        val isFirstLaunch = it.isAppFirstLaunch
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }.first()

    override suspend fun setThemeConfig(theme: ThemeConfig) {
       settings.updateData {
           it.copy(theme = theme)
       }
    }

    override suspend fun getAppSettingsFlow(): Flow<AppSettings> = settings.data
}