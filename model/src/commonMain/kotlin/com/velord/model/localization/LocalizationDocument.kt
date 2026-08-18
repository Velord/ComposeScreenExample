package com.velord.model.localization

@JvmInline
value class LocalizationStrings(
    val value: Map<String, String>,
) {
    operator fun get(key: String): String? = value[key]
    val keys: Set<String> get() = value.keys
}

data class LocalizationDocument(
    val schemaVersion: Int,
    val defaultLanguage: LanguageCode,
    val languages: Map<LanguageCode, LocalizationStrings>,
)
