package com.velord.data.gateway.setting

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.datastore.DataStoreDataSource
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.CancellationException
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
            try {
                appState.languagePreferenceFlow.value = dataStore
                    .getAppSettingFlow()
                    .map { it.language }
                    .first()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                // Keep the in-memory default if persisted settings cannot be read.
            }
            isInitialized = true
        }

        return appState.languagePreferenceFlow
    }

    suspend fun get(): LanguagePreference = getFlow().first()

    suspend fun save(language: LanguagePreference) {
        try {
            dataStore.setLanguagePreference(language)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            // Keep the current session responsive even if persistence is temporarily unavailable.
        }
        appState.languagePreferenceFlow.value = language
    }
}
