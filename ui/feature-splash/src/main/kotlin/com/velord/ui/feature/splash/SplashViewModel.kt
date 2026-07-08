package com.velord.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val SPLASH_DELAY_MS = 2000L

class SplashViewModel : ViewModel() {

    val isAppReadyFlow = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS.milliseconds)
            isAppReadyFlow.value = true
        }
    }
}
