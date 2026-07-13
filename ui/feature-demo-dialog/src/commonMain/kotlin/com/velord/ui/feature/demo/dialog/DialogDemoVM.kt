package com.velord.ui.feature.demo.dialog

import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DialogDemoUiState(
    val isVisibleTwoButtonDialog: Boolean,
    val isVisibleOneButtonDialog: Boolean,
) {
    companion object {
        val DEFAULT = DialogDemoUiState(
            isVisibleTwoButtonDialog = false,
            isVisibleOneButtonDialog = false,
        )
    }
}

sealed interface DialogDemoUiAction {
    data object OpenTwoButtonDialogClick : DialogDemoUiAction
    data object TwoButtonDialogDismiss : DialogDemoUiAction
    data object OpenOneButtonDialogClick : DialogDemoUiAction
    data object OneButtonDialogDismiss : DialogDemoUiAction
}

class DialogDemoVM : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(DialogDemoUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<DialogDemoUiAction>()

    init {
        observe()
    }

    fun onAction(action: DialogDemoUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onOpenTwoButtonDialogClick() {
        uiStateFlow.update { state -> state.copy(isVisibleTwoButtonDialog = true) }
    }

    private fun onTwoButtonDialogDismiss() {
        uiStateFlow.update { state -> state.copy(isVisibleTwoButtonDialog = false) }
    }

    private fun onOpenOneButtonDialogClick() {
        uiStateFlow.update { state -> state.copy(isVisibleOneButtonDialog = true) }
    }

    private fun onOneButtonDialogDismiss() {
        uiStateFlow.update { state -> state.copy(isVisibleOneButtonDialog = false) }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is DialogDemoUiAction.OpenTwoButtonDialogClick -> onOpenTwoButtonDialogClick()
                    is DialogDemoUiAction.TwoButtonDialogDismiss -> onTwoButtonDialogDismiss()
                    is DialogDemoUiAction.OpenOneButtonDialogClick -> onOpenOneButtonDialogClick()
                    is DialogDemoUiAction.OneButtonDialogDismiss -> onOneButtonDialogDismiss()
                }
            }
        }
    }
}
