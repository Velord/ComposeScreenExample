package com.velord.datastore.appSetting

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.velord.datastore.appSettingStorePath
import com.velord.model.setting.AppSetting
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import org.koin.core.annotation.Single

@Single
class AppSettingDataStoreImpl : AppSettingDataStore {

    private val dataStore: DataStore<AppSetting> = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = AppSettingDataStoreSerializer,
            producePath = ::appSettingStorePath,
        ),
    )

    override val flow: Flow<AppSetting>
        get() = dataStore.data

    override suspend fun updateData(
        transform: suspend (value: AppSetting) -> AppSetting,
    ): Result<AppSetting> = Result.runCatching {
        dataStore.updateData { transform(it) }
    }
}
