package com.velord.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    val isAppReadyFlow = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            delay(2000)
            isAppReadyFlow.value = true
        }
    }
}
