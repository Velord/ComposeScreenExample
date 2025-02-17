package com.velord.datastore

import com.velord.datastore.appSettings.AppSettingsDataStore
import com.velord.model.settings.AppSettings
import com.velord.model.settings.ThemeConfig
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
    private val appSettings: AppSettingsDataStore
) : DataStoreService {

    private suspend fun setFirstLaunch() {
        appSettings.updateData {
            it.copy(isAppFirstLaunch = true)
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = appSettings.flow.map {
        val isFirstLaunch = it.isAppFirstLaunch
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }.first()

    override suspend fun setThemeConfig(theme: ThemeConfig) {
        appSettings.updateData {
           it.copy(theme = theme)
       }
    }

    override suspend fun getAppSettingsFlow(): Flow<AppSettings> = appSettings.flow
}