package com.velord.feature.demo

import com.velord.navigation.fragment.NavigationDataFragment
import com.velord.navigation.voyager.NavigationDataVoyager
import com.velord.navigation.voyager.SharedScreenVoyager
import com.velord.resource.R
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DemoViewModel : CoroutineScopeViewModel() {

    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataFragment>()
    val navigationEventDestination = MutableSharedFlow<DemoDest>()

    fun onOpenShape() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Shape))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_shapeDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Shape)
    }

    fun onOpenModifier() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Modifier))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_modifierDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Modifier)
    }

    fun onOpenSummator() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.FlowSummator))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_flowSummatorFragment)
        )
        navigationEventDestination.emit(DemoDest.FlowSummator)
    }

    fun onOpenMorph() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Morph))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_morphDemoFragment)
        )
        navigationEventDestination.emit(DemoDest.Morph)
    }

    fun onOpenHintPhoneNumber() = launch {
        // TODO: Add for Voyager and Jetpack
        navigationEventDestination.emit(DemoDest.HintPhoneNumber)
    }

    fun onOpenMovie() = launch {
        // TODO: Add for Voyager and Jetpack
        navigationEventDestination.emit(DemoDest.Movie)
    }
}