package com.velord.usecase.localization

import com.velord.model.setting.LanguagePreference

fun interface SetLanguagePreferenceUC : suspend (LanguagePreference) -> Unit
