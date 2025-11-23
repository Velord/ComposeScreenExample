package com.velord.datastore

import com.velord.datastore.appSetting.AppSettingDataStore
import com.velord.model.setting.AppSetting
import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

interface DataStoreService {
    suspend fun checkAppFirstLaunch(): Boolean
    suspend fun setThemeConfig(theme: ThemeConfig)
    suspend fun getAppSettingFlow(): Flow<AppSetting>
}

@Single
class DataStoreServiceImpl(
    private val appSetting: AppSettingDataStore
) : DataStoreService {

    private suspend fun setFirstLaunch() {
        appSetting.updateData {
            it.copy(isAppFirstLaunch = true)
        }
    }

    override suspend fun checkAppFirstLaunch(): Boolean = appSetting.flow.map {
        val isFirstLaunch = it.isAppFirstLaunch
        if (isFirstLaunch) setFirstLaunch()

        isFirstLaunch
    }.first()

    override suspend fun setThemeConfig(theme: ThemeConfig) {
        appSetting.updateData {
           it.copy(theme = theme)
       }
    }

    override suspend fun getAppSettingFlow(): Flow<AppSetting> = appSetting.flow
}