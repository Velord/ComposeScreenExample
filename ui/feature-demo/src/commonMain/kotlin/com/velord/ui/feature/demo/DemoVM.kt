package com.velord.ui.feature.demo

import com.velord.core.resource.Res
import com.velord.core.resource.this_demo_is_deprecated
import com.velord.infrastructure.config.BuildConfigResolver
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.ShowToastUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

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

    private fun onOpenShape() = navigateTo(DemoNavigationEvent.Shape)

    private fun onOpenModifier() = navigateTo(DemoNavigationEvent.Modifier)

    private fun onOpenSummator() = navigateTo(DemoNavigationEvent.FlowSummator)

    private fun onOpenMorph() = navigateTo(DemoNavigationEvent.Morph)

    private fun onOpenHintPhoneNumber() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.HintPhoneNumber)
    }

    private fun onOpenMovie() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.Movie)
    }

    private fun onOpenDialog() = launch {
        checkJetpackLib()
        navigationEvent.emit(DemoNavigationEvent.Dialog)
    }

    private fun navigateTo(destination: DemoNavigationEvent) = launch {
        navigationEvent.emit(destination)
    }

    private suspend fun checkJetpackLib() {
        val lib = buildConfigResolver.getNavigationLib()
        if (lib.isJetpack) {
            val message = getString(Res.string.this_demo_is_deprecated, lib.name)
            val toastConfig = ToastConfig(message = message, duration = ToastDuration.Long)
            showToastUC(toastConfig)
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
