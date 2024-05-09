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
    val navigationEventDestination = MutableSharedFlow<DemoDest>()

    fun onOpenShape() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.Shape))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_shapeDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Shape)
    }

    fun onOpenModifier() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.Modifier))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_modifierDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Modifier)
    }

    fun onOpenSummator() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.FlowSummator))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_flowSummatorFragment)
        )
        navigationEventDestination.emit(DemoDest.FlowSummator)
    }

    fun onOpenMorph() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreen.Demo.Morph))
        navigationEventJetpack.emit(
            NavigationDataJetpack(com.velord.resource.R.id.from_demoFragment_to_morphDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Morph)
    }
}