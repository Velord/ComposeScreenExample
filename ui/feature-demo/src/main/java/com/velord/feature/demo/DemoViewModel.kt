package com.velord.feature.demo

import com.velord.navigation.fragment.NavigationDataFragment
import com.velord.navigation.voyager.NavigationDataVoyager
import com.velord.navigation.voyager.SharedScreenVoyager
import com.velord.resource.R
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

sealed interface DemoUiAction {
    data object OpenShapeClick : DemoUiAction
    data object OpenModifierClick : DemoUiAction
    data object OpenSummatorClick : DemoUiAction
    data object OpenMorphClick : DemoUiAction
    data object OpenHintPhoneNumberClick : DemoUiAction
    data object OpenMovieClick : DemoUiAction
}

class DemoViewModel : CoroutineScopeViewModel() {

    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataFragment>()
    val navigationEventDestination = MutableSharedFlow<DemoDestinationNavigationEvent>()
    private val actionFlow = MutableSharedFlow<DemoUiAction>()

    init {
        observe()
    }

    fun onAction(action: DemoUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onOpenShape() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Shape))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_shapeDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Shape)
    }

    private fun onOpenModifier() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Modifier))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_modifierDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Modifier)
    }

    private fun onOpenSummator() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.FlowSummator))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_flowSummatorFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.FlowSummator)
    }

    private fun onOpenMorph() = launch {
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Morph))
        navigationEventJetpack.emit(
            NavigationDataFragment(R.id.from_demoFragment_to_morphDemoFragment)
        )
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Morph)
    }

    private fun onOpenHintPhoneNumber() = launch {
        // TODO: Add for Jetpack
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.HintPhoneNumber))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.HintPhoneNumber)
    }

    private fun onOpenMovie() = launch {
        // TODO: Add for Jetpack
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Movie))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Movie)
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    DemoUiAction.OpenShapeClick -> onOpenShape()
                    DemoUiAction.OpenModifierClick -> onOpenModifier()
                    DemoUiAction.OpenSummatorClick -> onOpenSummator()
                    DemoUiAction.OpenMorphClick -> onOpenMorph()
                    DemoUiAction.OpenHintPhoneNumberClick -> onOpenHintPhoneNumber()
                    DemoUiAction.OpenMovieClick -> onOpenMovie()
                }
            }
        }
    }
}