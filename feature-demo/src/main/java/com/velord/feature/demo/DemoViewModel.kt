package com.velord.feature.demo

import com.velord.navigation.NavigationDataJetpack
import com.velord.navigation.NavigationDataVoyager
import com.velord.navigation.SharedScreen
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DemoViewModel : CoroutineScopeViewModel() {

    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataJetpack>()

    fun onOpenShape() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.Shape))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_shapeDemoFragment)
        )
    }

    fun onOpenModifier() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.Modifier))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_modifierDemoFragment)
        )
    }

    fun onOpenSummator() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.FlowSummator))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_flowSummatorFragment)
        )
    }
}