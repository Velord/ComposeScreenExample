package com.velord.model.localization

import com.velord.model.setting.LanguagePreference

data class LocalizationState(
    val document: LocalizationDocument,
    val language: LanguageCode,
    val preference: LanguagePreference,
)
