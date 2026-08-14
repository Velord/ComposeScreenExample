package com.velord.core.resource

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val SUPPORTED_SCHEMA_VERSION = 1
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
        remote.schemaVersion == bundled.schemaVersion &&
            remote.languages.keys == bundled.languages.keys &&
            remote.languages.all { (language, strings) ->
                strings.keys == bundled.languages.getValue(language).keys
            }
    }

    private fun validate(document: LocalizationDocument) {
        require(document.schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "Unsupported localization schema: ${document.schemaVersion}"
        }
        require(document.languages.containsKey("en")) {
            "Localization must contain English"
        }
        require(document.languages.containsKey("es")) {
            "Localization must contain Spanish"
        }

        val english = document.languages.getValue("en")
        require(english.isNotEmpty()) {
            "Localization must contain strings"
        }

        document.languages.forEach { (language, strings) ->
            require(strings.keys == english.keys) {
                "Localization key mismatch for language: $language"
            }
            strings.forEach { (key, value) ->
                require(value.isNotEmpty()) {
                    "Localization value is empty: $language/$key"
                }
                require(placeholders(value) == placeholders(english.getValue(key))) {
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
