package com.velord.ui.feature.splash

import com.velord.core.resource.LocalizationRuntime
import com.velord.core.resource.readBundledLocalizationJson
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.ui.sharedviewmodel.UiContractExempt
import com.velord.ui.sharedviewmodel.UiContractExemptionReason
import com.velord.usecase.localization.FetchLocalizationUC
import com.velord.usecase.localization.InitializeLocalizationUC
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SPLASH_DELAY_MS = 2000L

@UiContractExempt(UiContractExemptionReason.SimpleFlow)
class SplashVM(
    private val initializeLocalizationUC: InitializeLocalizationUC,
    private val fetchLocalizationUC: FetchLocalizationUC,
) : CoroutineScopeVM() {

    val isAppReadyFlow = MutableStateFlow(false)

    init {
        launch {
            val splashDelayJob = launch {
                delay(SPLASH_DELAY_MS.milliseconds)
            }
            val bundledLocalization = readBundledLocalizationJson()
            val startup = initializeLocalizationUC(bundledLocalization)
            LocalizationRuntime.initialize(
                bundledJson = bundledLocalization,
                remoteJson = startup.remoteJson,
                preference = startup.languagePreference,
            )
            launch {
                fetchLocalizationUC()
            }
            joinAll(splashDelayJob)
            isAppReadyFlow.value = true
        }
    }
}
