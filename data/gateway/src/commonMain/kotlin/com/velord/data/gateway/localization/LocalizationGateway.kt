package com.velord.data.gateway.localization

import co.touchlab.kermit.Logger
import com.velord.data.appstate.AppStateDataSource
import com.velord.data.gateway.setting.LanguagePreferenceGateway
import com.velord.data.localization.LocalizationDataSource
import com.velord.model.localization.LocalizationDocument
import com.velord.model.localization.LocalizationState
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private val log = Logger.withTag("LocalizationGateway")

@Single
class LocalizationGateway(
    private val localizationDataSource: LocalizationDataSource,
    private val languagePreferenceGateway: LanguagePreferenceGateway,
    private val appState: AppStateDataSource,
) {
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        log.e(throwable) { "Unhandled error in LocalizationGateway coroutine scope" }
        localizationDataSource.recordException(throwable)
    }
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default + errorHandler
    )

    suspend fun initialize() {
        if (appState.localizationStateFlow.value != LocalizationState.DEFAULT) return

        // Activate bundled (or previously activated remote) instantly
        val bundled = localizationDataSource.getBundled()
        val initial = localizationDataSource.getActivatedRemote(bundled) ?: bundled
        publishLocalization(initial)

        // Fetch new updates in background
        scope.launch {
            fetchAndPublishLocalization(bundled)
        }
    }

    fun getStateFlow(): StateFlow<LocalizationState> = appState.localizationStateFlow

    fun getLanguagePreferenceFlow(): Flow<LanguagePreference> = languagePreferenceGateway.getFlow()

    suspend fun setLanguagePreference(preference: LanguagePreference) {
        val current = appState.localizationStateFlow.value
        val deviceLanguage = localizationDataSource.currentLanguageCode()
        val language = current.document.resolveLanguage(
            preference = preference,
            currentDeviceLanguage = deviceLanguage
        )

        languagePreferenceGateway.save(preference)
        appState.localizationStateFlow.update {
            it.copy(language = language)
        }
    }

    private suspend fun fetchAndPublishLocalization(bundled: LocalizationDocument) {
        try {
            localizationDataSource.fetchAndActivate()
            val activated = localizationDataSource.getActivatedRemote(bundled)
            if (activated != null) {
                publishLocalization(activated)
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            log.w(error) { "Failed to fetch remote localization from Remote Config" }
        }
    }

    private suspend fun publishLocalization(document: LocalizationDocument) {
        val preference = languagePreferenceGateway.get()
        val deviceLanguage = localizationDataSource.currentLanguageCode()
        val language = document.resolveLanguage(
            preference = preference,
            currentDeviceLanguage = deviceLanguage
        )

        val localizationState = LocalizationState(
            document = document,
            language = language,
        )
        appState.localizationStateFlow.value = localizationState
    }
}
