package com.velord.model.localization

import com.velord.model.setting.LanguagePreference

data class LocalizationStartup(
    val remoteJson: String?,
    val languagePreference: LanguagePreference,
)
