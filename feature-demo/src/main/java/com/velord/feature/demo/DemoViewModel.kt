package com.velord.feature.demo

import com.example.sharedviewmodel.CoroutineScopeViewModel
import com.velord.navigation.NavigationData
import com.velord.navigation.SharedScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DemoViewModel : CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    fun onOpenShape() = launch {
        navigationEvent.emit(NavigationData(SharedScreen.Demo.Shape))
    }

    fun onOpenModifier() = launch {
        navigationEvent.emit(NavigationData(SharedScreen.Demo.Modifier))
    }

    fun onOpenSummator() = launch {
        navigationEvent.emit(NavigationData(SharedScreen.Demo.FlowSummator))
    }
}