package com.velord.data.gateway.localization

import com.velord.core.resource.Res
import com.velord.data.appstate.AppStateDataSource
import com.velord.data.gateway.setting.LanguagePreferenceGateway
import com.velord.data.localization.LocalizationDataSource
import com.velord.model.localization.LanguageCode
import com.velord.model.localization.LocalizationDocument
import com.velord.model.localization.LocalizationState
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.annotation.Single

private const val LOCALIZATION_RESOURCE_PATH = "files/localization.json"

@Single
class LocalizationGateway(
    private val localizationDataSource: LocalizationDataSource,
    private val languagePreferenceGateway: LanguagePreferenceGateway,
    private val appState: AppStateDataSource,
) {

    suspend fun initialize() {
        if (appState.localizationStateFlow.value != null) return

        val bundled = localizationDataSource
            .parse(readBundledLocalizationJson())
            .getOrThrow()
        val document = fetchRemoteLocalization(bundled) ?: bundled
        val preference = languagePreferenceGateway.get()

        appState.localizationStateFlow.value = LocalizationState(
            document = document,
            language = resolveLanguage(preference, document),
            preference = preference,
        )
    }

    fun getStateFlow(): StateFlow<LocalizationState?> = appState.localizationStateFlow

    suspend fun setLanguagePreference(preference: LanguagePreference) {
        val current = appState.localizationStateFlow.value ?: return
        languagePreferenceGateway.save(preference)
        appState.localizationStateFlow.value = current.copy(
            language = resolveLanguage(preference, current.document),
            preference = preference,
        )
    }

    private suspend fun fetchRemoteLocalization(
        bundled: LocalizationDocument,
    ): LocalizationDocument? = try {
        localizationDataSource
            .fetchLocalization()
            ?.let { localizationDataSource.parseRemote(it, bundled) }
    } catch (error: CancellationException) {
        throw error
    } catch (_: Exception) {
        null
    }

    private fun resolveLanguage(
        preference: LanguagePreference,
        document: LocalizationDocument,
    ): LanguageCode {
        val requested = if (preference.isDefault) {
            localizationDataSource.currentLanguageCode().value
        } else {
            preference.languageCode
        }

        return document.findLanguage(requested)
            ?: document.findLanguage(requested.substringBefore('-').substringBefore('_'))
            ?: document.defaultLanguage
    }

    private fun LocalizationDocument.findLanguage(languageTag: String): LanguageCode? = languages
        .keys
        .firstOrNull { language -> language.value.equals(languageTag, ignoreCase = true) }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun readBundledLocalizationJson(): String = Res
        .readBytes(LOCALIZATION_RESOURCE_PATH)
        .decodeToString()
}
