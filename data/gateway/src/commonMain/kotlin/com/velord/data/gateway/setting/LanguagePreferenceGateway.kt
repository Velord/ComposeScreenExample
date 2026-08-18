package com.velord.data.gateway.setting

import com.velord.data.datastore.DataStoreDataSource
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class LanguagePreferenceGateway(
    private val dataStore: DataStoreDataSource,
) {

    fun getFlow(): Flow<LanguagePreference> = dataStore
        .getAppSettingFlow()
        .map { it.language }

    suspend fun get(): LanguagePreference = getFlow().first()

    suspend fun save(language: LanguagePreference) {
        dataStore.setLanguagePreference(language)
    }
}
