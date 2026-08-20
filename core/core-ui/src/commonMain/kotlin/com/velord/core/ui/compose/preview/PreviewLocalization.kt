package com.velord.core.ui.compose.preview

import com.velord.model.localization.LocalizationDocument
import com.velord.model.localization.LocalizationState
import com.velord.model.localization.LocalizationStringMap
import com.velord.model.setting.LanguagePreference

private val previewLanguageRoster = LanguagePreference.entries
    .filterNot { it.isDefault }
    .associate { preference -> preference.languageCode to LocalizationStringMap(emptyMap()) }
private val previewLanguage = previewLanguageRoster.keys.first()

val previewLocalizationState = LocalizationState(
    document = LocalizationDocument(
        schemaVersion = 1,
        defaultLanguage = previewLanguage,
        languageRoster = previewLanguageRoster,
    ),
    language = previewLanguage,
)
