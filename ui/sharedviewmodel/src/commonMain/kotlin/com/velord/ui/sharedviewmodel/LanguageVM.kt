package com.velord.ui.sharedviewmodel

import com.velord.model.localization.LocalizationState
import com.velord.model.setting.LanguagePreference
import com.velord.usecase.setting.GetLocalizationStateUC
import com.velord.usecase.setting.SetLanguagePreferenceUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class LanguageUiState(
    val localization: LocalizationState? = null,
)

sealed interface LanguageUiAction {
    data class Select(val preference: LanguagePreference) : LanguageUiAction
}

class LanguageVM(
    private val getLocalizationStateUC: GetLocalizationStateUC,
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
            getLocalizationStateUC().collect { localization ->
                uiStateFlow.value = LanguageUiState(localization)
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
