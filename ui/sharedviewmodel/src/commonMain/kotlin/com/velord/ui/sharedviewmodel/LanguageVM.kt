package com.velord.ui.sharedviewmodel

import com.velord.core.resource.LocalizationRuntime
import com.velord.model.setting.LanguagePreference
import com.velord.usecase.setting.GetLanguagePreferenceUC
import com.velord.usecase.setting.SetLanguagePreferenceUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class LanguageUiState(
    val preference: LanguagePreference = LanguagePreference.DEFAULT,
)

sealed interface LanguageUiAction {
    data class Select(val preference: LanguagePreference) : LanguageUiAction
}

class LanguageVM(
    private val getLanguagePreferenceUC: GetLanguagePreferenceUC,
    private val setLanguagePreferenceUC: SetLanguagePreferenceUC,
) : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(LanguageUiState())
    private val actionFlow = MutableSharedFlow<LanguageUiAction>()

    init {
        observe()
    }

    fun onAction(action: LanguageUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun observe() {
        launch {
            getLanguagePreferenceUC().collect { preference ->
                LocalizationRuntime.setLanguagePreference(preference)
                uiStateFlow.value = LanguageUiState(preference)
            }
        }
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is LanguageUiAction.Select -> setLanguagePreferenceUC(action.preference)
                }
            }
        }
    }
}
