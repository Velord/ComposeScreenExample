package com.velord.dialogDemo

import com.velord.sharedviewmodel.CoroutineScopeViewModel
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


class DialogDemoViewModel : CoroutineScopeViewModel() {

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

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is DialogDemoUiAction.OpenTwoButtonDialogClick ->
                        uiStateFlow.update { it.copy(isVisibleTwoButtonDialog = true) }
                    is DialogDemoUiAction.TwoButtonDialogDismiss ->
                        uiStateFlow.update { it.copy(isVisibleTwoButtonDialog = false) }
                    is DialogDemoUiAction.OpenOneButtonDialogClick ->
                        uiStateFlow.update { it.copy(isVisibleOneButtonDialog = true) }
                    is DialogDemoUiAction.OneButtonDialogDismiss ->
                        uiStateFlow.update { it.copy(isVisibleOneButtonDialog = false) }
                }
            }
        }
    }
}
