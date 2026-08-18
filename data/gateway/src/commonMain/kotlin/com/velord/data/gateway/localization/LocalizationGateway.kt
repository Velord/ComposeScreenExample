package com.velord.data.gateway.localization

import com.velord.core.resource.LocalizationRuntime
import com.velord.core.resource.readBundledLocalizationJson
import com.velord.data.firebase.FirebaseRemoteConfigDataSource
import com.velord.data.gateway.setting.LanguagePreferenceGateway
import kotlinx.coroutines.CancellationException
import org.koin.core.annotation.Single

@Single
class LocalizationGateway(
    private val remoteConfig: FirebaseRemoteConfigDataSource,
    private val languagePreferenceGateway: LanguagePreferenceGateway,
) {

    suspend fun initialize() {
        val bundledLocalization = readBundledLocalizationJson()
        val remoteLocalization = try {
            remoteConfig.initialize(bundledLocalization)
            remoteConfig.getLocalization()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            null
        }

        LocalizationRuntime.initialize(
            bundledJson = bundledLocalization,
            remoteJson = remoteLocalization,
            preference = languagePreferenceGateway.get(),
        )
    }

    suspend fun fetchAndActivate() {
        try {
            remoteConfig.fetchAndActivate()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            // The current session already has a validated bundled/activated localization document.
        }
    }
}
