package com.velord.core.resource

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val SUPPORTED_SCHEMA_VERSION = 1
private const val DEFAULT_LANGUAGE = "en"
private val PLACEHOLDER_REGEX = Regex("""%(\d+)\${'$'}([sd])""")

@Serializable
internal data class LocalizationDocument(
    val schemaVersion: Int,
    val languages: Map<String, Map<String, String>>,
)

internal object LocalizationDocumentParser {

    private val json = Json {
        ignoreUnknownKeys = false
    }

    fun parse(value: String): Result<LocalizationDocument> = runCatching {
        val document = json.decodeFromString<LocalizationDocument>(value)
        validate(document)
        document
    }

    fun parseRemote(
        value: String,
        bundled: LocalizationDocument,
    ): LocalizationDocument? = parse(value).getOrNull()?.takeIf { remote ->
        val bundledKeys = bundled.languages.getValue(DEFAULT_LANGUAGE).keys
        remote.schemaVersion == bundled.schemaVersion &&
            remote.languages.keys.containsAll(bundled.languages.keys) &&
            remote.languages.values.all { strings -> strings.keys == bundledKeys }
    }

    private fun validate(document: LocalizationDocument) {
        require(document.schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "Unsupported localization schema: ${document.schemaVersion}"
        }
        require(document.languages.isNotEmpty()) {
            "Localization must contain at least one language"
        }
        require(document.languages.containsKey(DEFAULT_LANGUAGE)) {
            "Localization must contain default language: $DEFAULT_LANGUAGE"
        }

        val defaultStrings = document.languages.getValue(DEFAULT_LANGUAGE)
        require(defaultStrings.isNotEmpty()) {
            "Localization must contain strings"
        }

        document.languages.forEach { (language, strings) ->
            require(strings.keys == defaultStrings.keys) {
                "Localization key mismatch for language: $language"
            }
            strings.forEach { (key, value) ->
                require(value.isNotEmpty()) {
                    "Localization value is empty: $language/$key"
                }
                require(placeholders(value) == placeholders(defaultStrings.getValue(key))) {
                    "Localization placeholder mismatch: $language/$key"
                }
            }
        }
    }

    private fun placeholders(value: String): List<String> = PLACEHOLDER_REGEX
        .findAll(value)
        .map { it.value }
        .sorted()
        .toList()
}
