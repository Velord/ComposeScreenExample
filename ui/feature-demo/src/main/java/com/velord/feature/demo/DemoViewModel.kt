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
    val navigationEventDestination = MutableSharedFlow<DemoDestinationNavigationEvent>()

    fun onOpenShape() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Shape))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_shapeDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Shape)
    }

    fun onOpenModifier() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Modifier))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_modifierDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Modifier)
    }

    fun onOpenSummator() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.FlowSummator))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_flowSummatorFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.FlowSummator)
    }

    fun onOpenMorph() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Morph))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_morphDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Morph)
    }

    fun onOpenHintPhoneNumber() = launch {
        // TODO: Add for Jetpack
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.HintPhoneNumber))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.HintPhoneNumber)
    }

    fun onOpenMovie() = launch {
        // TODO: Add for Jetpack
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Movie))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Movie)
    }
}