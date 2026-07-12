package com.velord.ui.feature.splash

import com.velord.ui.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SPLASH_DELAY_MS = 2000L

class SplashViewModel : CoroutineScopeViewModel() {

    val isAppReadyFlow = MutableStateFlow(false)

    init {
        launch {
            delay(SPLASH_DELAY_MS.milliseconds)
            isAppReadyFlow.value = true
        }
    }
}
