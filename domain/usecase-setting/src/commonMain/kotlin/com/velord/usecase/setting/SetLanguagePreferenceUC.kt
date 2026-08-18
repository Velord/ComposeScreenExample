package com.velord.usecase.setting

import com.velord.model.setting.LanguagePreference

fun interface SetLanguagePreferenceUC : suspend (LanguagePreference) -> Unit
