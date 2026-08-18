package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.remoteconfig.remoteConfig
import com.velord.model.localization.LanguageCode
import com.velord.model.localization.LocalizationDocument
import com.velord.model.localization.LocalizationStrings
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.annotation.Single

private const val SUPPORTED_SCHEMA_VERSION = 1
private const val LOCALIZATION_PARAMETER = "localization"
private val PLACEHOLDER_REGEX = Regex("""%([0-9]+)[$]([sd])""")

@Single
class LocalizationDataSource {

    private val json = Json {
        ignoreUnknownKeys = false
    }

    suspend fun fetchLocalization(): String? {
        initializeRemoteConfigPlatform()
        Firebase.remoteConfig.fetchAndActivate()
        return Firebase.remoteConfig
            .getString(LOCALIZATION_PARAMETER)
            ?.takeIf(String::isNotBlank)
    }

    fun parse(value: String): Result<LocalizationDocument> = runCatching {
        val root = json.parseToJsonElement(value).jsonObject
        val schemaVersion = root.getValue("schemaVersion").jsonPrimitive.content.toInt()
        val languageObject = root.getValue("languages").jsonObject

        val document = LocalizationDocument(
            schemaVersion = schemaVersion,
            languages = languageObject.map { (language, rawStrings) ->
                LanguageCode(language) to LocalizationStrings(rawStrings.jsonObject.toStringMap(language))
            }.toMap(),
        )
        validate(document)
        document
    }

    fun parseRemote(
        value: String,
        bundled: LocalizationDocument,
    ): LocalizationDocument? = parse(value).getOrNull()?.takeIf { remote ->
        val bundledKeys = bundled.languages.getValue(LanguageCode.English).keys
        remote.schemaVersion == bundled.schemaVersion &&
            remote.languages.keys.containsAll(bundled.languages.keys) &&
            remote.languages.values.all { strings -> strings.keys == bundledKeys }
    }

    fun currentLanguageCode(): LanguageCode = LanguageCode(
        currentLanguageTag()
            .lowercase()
            .substringBefore('-')
            .substringBefore('_'),
    )

    private fun validate(document: LocalizationDocument) {
        require(document.schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "Unsupported localization schema: ${document.schemaVersion}"
        }
        require(document.languages.isNotEmpty()) {
            "Localization must contain at least one language"
        }

        val defaultStrings = requireNotNull(document.languages[LanguageCode.English]) {
            "Localization must contain fallback language: ${LanguageCode.English.value}"
        }
        require(defaultStrings.value.isNotEmpty()) {
            "Localization must contain strings"
        }

        document.languages.forEach { (language, strings) ->
            require(strings.keys == defaultStrings.keys) {
                "Localization key mismatch for language: ${language.value}"
            }
            strings.value.forEach { (key, value) ->
                require(value.isNotEmpty()) {
                    "Localization value is empty: ${language.value}/$key"
                }
                require(placeholders(value) == placeholders(defaultStrings.value.getValue(key))) {
                    "Localization placeholder mismatch: ${language.value}/$key"
                }
            }
        }
    }

    private fun JsonObject.toStringMap(language: String): Map<String, String> = mapValues { (key, value) ->
        val primitive = value.jsonPrimitive
        require(primitive.isString) {
            "Localization value must be a string: $language/$key"
        }
        primitive.content
    }

    private fun placeholders(value: String): List<String> = PLACEHOLDER_REGEX
        .findAll(value)
        .map { it.value }
        .sorted()
        .toList()
}
