package com.velord.datastore

import androidx.datastore.core.DataStore
import com.velord.util.settings.AppSettings
import com.velord.util.settings.ThemeConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DataStoreService {
    suspend fun checkAppFirstLaunch(): Boolean
    suspend fun setThemeConfig(theme: ThemeConfig)
    suspend fun getThemeConfig(): ThemeConfig
}

class DataStoreServiceImpl(
    private val dataStore: DataStore<AppSettings>
) : DataStoreService {

    private suspend fun setFirstLaunch() {
        dataStore.updateData {
            it.copy(isAppFirstLaunch = true)
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = dataStore.data.map {
        val isFirstLaunch = it.isAppFirstLaunch
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }.first()

    override suspend fun setThemeConfig(theme: ThemeConfig) {
       dataStore.updateData {
           it.copy(theme = theme)
       }
    }

    override suspend fun getThemeConfig(): ThemeConfig = dataStore.data.first().theme
}