package com.velord.datastore.appSettings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.velord.util.settings.AppSettings
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface AppSettingsDataStore : DataStore<AppSettings>

@Single
class AppSettingsDataStoreImpl(private val context: Context) : AppSettingsDataStore {

    private val Context.dataStore by dataStore(
        fileName = "settings",
        serializer = AppSettingsDataStoreSerializer
    )

    override val data: Flow<AppSettings>
        get() = context.dataStore.data

    override suspend fun updateData(
        transform: suspend (t: AppSettings) -> AppSettings
    ): AppSettings = context.dataStore.updateData {
        transform(it)
    }
}