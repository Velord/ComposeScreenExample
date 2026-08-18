package com.velord.usecase.setting

import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.flow.Flow

fun interface GetLanguagePreferenceUC : suspend () -> Flow<LanguagePreference>
