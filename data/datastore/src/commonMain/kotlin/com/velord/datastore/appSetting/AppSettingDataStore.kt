package com.velord.datastore.appSetting

import com.velord.model.setting.AppSetting
import kotlinx.coroutines.flow.Flow

interface AppSettingDataStore {

    val flow: Flow<AppSetting>

    suspend fun updateData(
        transform: suspend (value: AppSetting) -> AppSetting,
    ): Result<AppSetting>
}
