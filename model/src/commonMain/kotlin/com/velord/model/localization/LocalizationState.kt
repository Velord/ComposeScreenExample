package com.velord.model.localization

import com.velord.model.setting.LanguagePreference

data class LocalizationState(
    val document: LocalizationDocument,
    val language: LanguageCode,
) {
    companion object {
        val DEFAULT = LocalizationState(
            document = LocalizationDocument.DEFAULT,
            language = LanguagePreference.ENGLISH.languageCode,
        )
    }
}


