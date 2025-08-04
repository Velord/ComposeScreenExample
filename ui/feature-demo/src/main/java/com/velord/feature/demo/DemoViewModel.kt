package com.velord.feature.demo

import android.content.Context
import com.velord.config.BuildConfigResolver
import com.velord.core.resource.R
import com.velord.navigation.fragment.NavigationDataFragment
import com.velord.navigation.voyager.NavigationDataVoyager
import com.velord.navigation.voyager.SharedScreenVoyager
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
    data object OpenDialogClick : DemoUiAction
}


class DemoViewModel(
    private val context: Context,
    private val buildConfigResolver: BuildConfigResolver
) : CoroutineScopeViewModel() {

    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataFragment>()
    val navigationEventDestination = MutableSharedFlow<DemoDestinationNavigationEvent>()
    val toastEvent = MutableSharedFlow<String>()

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
        checkJetpackLib()
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.HintPhoneNumber))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.HintPhoneNumber)
    }

    private fun onOpenMovie() = launch {
        checkJetpackLib()
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Movie))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Movie)
    }

    private fun onOpenDialog() = launch {
        checkJetpackLib()
        navigationEventVoyager.emit(NavigationDataVoyager(SharedScreenVoyager.Demo.Dialog))
        navigationEventDestination.emit(DemoDestinationNavigationEvent.Dialog)
    }

    private suspend fun checkJetpackLib() {
        val lib = buildConfigResolver.getNavigationLib()
        if (lib.isJetpack) {
            val str = context.getString(R.string.this_demo_is_deprecated, lib.name)
            toastEvent.emit(str)
        }
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
                    DemoUiAction.OpenDialogClick -> onOpenDialog()
                }
            }
        }
    }
}