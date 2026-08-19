package com.velord.ui.sharedviewmodel

import com.velord.model.localization.LocalizationState
import com.velord.model.setting.LanguagePreference
import com.velord.usecase.setting.GetLanguagePreferenceUC
import com.velord.usecase.setting.SetLanguagePreferenceUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class LanguageUiState(
    val localization: LocalizationState,
    val preference: LanguagePreference,
)

sealed interface LanguageUiAction {
    data class Select(val preference: LanguagePreference) : LanguageUiAction
}

class LanguageVM(
    private val getLanguagePreferenceUC: GetLanguagePreferenceUC,
    private val setLanguagePreferenceUC: SetLanguagePreferenceUC,
) : LocalizationVM() {

    val uiStateFlow = MutableStateFlow(
        LanguageUiState(
            localization = localizationStateFlow.value,
            preference = LanguagePreference.DEFAULT,
        ),
    )
    private val actionFlow = MutableSharedFlow<LanguageUiAction>()

    init {
        observe()
    }

    fun onAction(action: LanguageUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onSelect(preference: LanguagePreference) = launch {
        setLanguagePreferenceUC(preference)
    }

    private fun observe() {
        launch {
            val languagePreferenceFlow = getLanguagePreferenceUC()
            val combinedUiStateFlow = combine(
                localizationStateFlow,
                languagePreferenceFlow,
            ) { localization, preference ->
                LanguageUiState(
                    localization = localization,
                    preference = preference,
                )
            }

            combinedUiStateFlow.collect { uiState ->
                uiStateFlow.value = uiState
            }
        }
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is LanguageUiAction.Select -> onSelect(action.preference)
                }
            }
        }
    }
}
