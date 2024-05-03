package com.velord.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    val iaAppReadyFlow = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            delay(4000)
            iaAppReadyFlow.value = true
        }
    }
}
