package com.velord.data.gateway.setting

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.datastore.DataStoreDataSource
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class LanguagePreferenceGateway(
    private val dataStore: DataStoreDataSource,
    private val appState: AppStateDataSource,
) {

    private var isInitialized = false

    suspend fun getFlow(): Flow<LanguagePreference> {
        if (isInitialized.not()) {
            isInitialized = true
            runCatching {
                appState.languagePreferenceFlow.value = dataStore
                    .getAppSettingFlow()
                    .map { it.language }
                    .first()
            }
        }

        return appState.languagePreferenceFlow
    }

    suspend fun get(): LanguagePreference = getFlow().first()

    suspend fun save(language: LanguagePreference) {
        runCatching {
            dataStore.setLanguagePreference(language)
        }
        appState.languagePreferenceFlow.value = language
    }
}
