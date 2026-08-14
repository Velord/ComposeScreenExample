package com.velord.usecase.localization

import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.flow.Flow

fun interface GetLanguagePreferenceUC : suspend () -> Flow<LanguagePreference>
