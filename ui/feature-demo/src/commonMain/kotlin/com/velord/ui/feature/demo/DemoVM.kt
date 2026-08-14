package com.velord.ui.feature.demo

import com.velord.core.resource.AppString
import com.velord.core.resource.getString
import com.velord.infrastructure.config.BuildConfigResolver
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.ShowToastUC
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

class DemoVM(
    private val buildConfigResolver: BuildConfigResolver,
    private val showToastUC: ShowToastUC,
) : CoroutineScopeVM() {

    val navigationEvent = MutableSharedFlow<DemoNavigationEvent>()
    private val actionFlow = MutableSharedFlow<DemoUiAction>()

    init {
        observe()
    }

    fun onAction(action: DemoUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onOpenShapeClick() = navigateTo(DemoNavigationEvent.Shape)

    private fun onOpenModifierClick() = navigateTo(DemoNavigationEvent.Modifier)

    private fun onOpenSummatorClick() = navigateTo(DemoNavigationEvent.FlowSummator)

    private fun onOpenMorphClick() = navigateTo(DemoNavigationEvent.Morph)

    private fun onOpenHintPhoneNumberClick() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.HintPhoneNumber)
    }

    private fun onOpenMovieClick() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.Movie)
    }

    private fun onOpenDialogClick() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.Dialog)
    }

    private fun navigateTo(destination: DemoNavigationEvent) = launch {
        navigationEvent.emit(destination)
    }

    private suspend fun checkJetpackLib() {
        val lib = buildConfigResolver.getNavigationLib()
        if (lib.isJetpack) {
            val message = getString(AppString.this_demo_is_deprecated, lib.name)
            val toastConfig = ToastConfig(message = message, duration = ToastDuration.Long)
            showToastUC(toastConfig)
        }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    DemoUiAction.OpenShapeClick -> onOpenShapeClick()
                    DemoUiAction.OpenModifierClick -> onOpenModifierClick()
                    DemoUiAction.OpenSummatorClick -> onOpenSummatorClick()
                    DemoUiAction.OpenMorphClick -> onOpenMorphClick()
                    DemoUiAction.OpenHintPhoneNumberClick -> onOpenHintPhoneNumberClick()
                    DemoUiAction.OpenMovieClick -> onOpenMovieClick()
                    DemoUiAction.OpenDialogClick -> onOpenDialogClick()
                }
            }
        }
    }
}
