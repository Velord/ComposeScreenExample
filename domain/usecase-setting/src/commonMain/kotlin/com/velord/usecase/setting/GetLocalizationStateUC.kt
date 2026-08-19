package com.velord.usecase.setting

import com.velord.model.localization.LocalizationState
import kotlinx.coroutines.flow.StateFlow

fun interface GetLocalizationStateUC : () -> StateFlow<LocalizationState>
