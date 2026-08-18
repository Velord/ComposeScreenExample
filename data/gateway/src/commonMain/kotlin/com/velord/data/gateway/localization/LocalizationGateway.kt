package com.velord.data.gateway.localization

import com.velord.core.resource.AppStringResource
import com.velord.core.resource.Res
import com.velord.core.resource.getString as resolveString
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

        val bundledJson = readBundledLocalizationJson()
        val bundled = localizationDataSource.parse(bundledJson).getOrThrow()
        val remote = fetchRemoteLocalization(bundled)
        val document = remote ?: bundled
        val preference = languagePreferenceGateway.get()

        appState.localizationStateFlow.value = LocalizationState(
            document = document,
            language = resolveLanguage(preference, document),
            preference = preference,
        )
    }

    fun getStateFlow(): StateFlow<LocalizationState?> = appState.localizationStateFlow

    fun getString(
        resource: AppStringResource,
        vararg formatArgs: Any,
    ): String = resolveString(
        localization = requireNotNull(appState.localizationStateFlow.value) {
            "Localization is not initialized"
        },
        resource = resource,
        formatArgs = formatArgs,
    )

    suspend fun setLanguagePreference(preference: LanguagePreference) {
        val current = appState.localizationStateFlow.value ?: return
        appState.localizationStateFlow.value = current.copy(
            language = resolveLanguage(preference, current.document),
            preference = preference,
        )

        try {
            languagePreferenceGateway.save(preference)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            // The active app state stays responsive even when persistence is temporarily unavailable.
        }
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
            localizationDataSource.currentLanguageCode()
        } else {
            LanguageCode(preference.languageCode)
        }
        return requested.takeIf(document.languages::containsKey) ?: document.defaultLanguage
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun readBundledLocalizationJson(): String = Res
        .readBytes(LOCALIZATION_RESOURCE_PATH)
        .decodeToString()
}
