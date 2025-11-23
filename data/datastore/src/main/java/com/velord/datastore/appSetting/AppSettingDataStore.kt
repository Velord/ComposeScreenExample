package com.velord.datastore.appSetting

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.velord.model.setting.AppSetting
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface AppSettingDataStore {
    val flow: Flow<AppSetting>
    suspend fun updateData(transform: suspend (t: AppSetting) -> AppSetting): Result<AppSetting>
}

@Single
class AppSettingDataStoreImpl(
    private val context: Context
) : AppSettingDataStore {

    private val Context.dataStore by dataStore(
        fileName = "setting",
        serializer = AppSettingDataStoreSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler {
            AppSetting.DEFAULT
        }
    )

    override val flow: Flow<AppSetting>
        get() = context.dataStore.data

    override suspend fun updateData(
        transform: suspend (t: AppSetting) -> AppSetting
    ) = Result.runCatching {
        context.dataStore.updateData {
            transform(it)
        }
    }
}