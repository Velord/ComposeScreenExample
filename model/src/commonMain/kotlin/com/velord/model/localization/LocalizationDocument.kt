package com.velord.model.localization

import com.velord.model.setting.LanguagePreference

@JvmInline
value class LocalizationStringMap(val value: Map<String, String>) {

    val keys: Set<String> get() = value.keys

    operator fun get(key: String): String? = value[key]
}

data class LocalizationDocument(
    val schemaVersion: Int,
    val defaultLanguage: LanguageCode,
    val languageRoster: Map<LanguageCode, LocalizationStringMap>,
) {

    fun resolveLanguage(
        preference: LanguagePreference,
        currentDeviceLanguage: LanguageCode,
    ): LanguageCode {
        val requestedLanguage = if (preference.isDefault) {
            currentDeviceLanguage
        } else {
            preference.languageCode
        }
        val requestedTag = requestedLanguage.value
        val baseTag = requestedTag
            .substringBefore('-')
            .substringBefore('_')
        return findLanguage(requestedTag)
            ?: findLanguage(baseTag)
            ?: defaultLanguage
    }

    fun findLanguage(languageTag: String): LanguageCode? = languageRoster
        .keys
        .firstOrNull { language -> language.value.equals(languageTag, ignoreCase = true) }

    companion object {
        val DEFAULT = LocalizationDocument(
            schemaVersion = 1,
            defaultLanguage = LanguagePreference.ENGLISH.languageCode,
            languageRoster = mapOf(
                LanguagePreference.ENGLISH.languageCode to LocalizationStringMap(emptyMap()),
            ),
        )
    }
}

