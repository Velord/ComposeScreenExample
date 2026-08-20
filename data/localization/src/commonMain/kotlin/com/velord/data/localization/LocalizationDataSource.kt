package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.crashlytics.crashlytics
import com.firebasekit.remoteconfig.remoteConfig
import com.velord.core.resource.Res
import com.velord.model.localization.LanguageCode
import com.velord.model.localization.LocalizationDocument
import com.velord.model.localization.LocalizationStringMap
import com.velord.model.setting.LanguagePreference
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.annotation.Single

private const val LOCALIZATION_RESOURCE_PATH = "files/localization.json"
private const val LOCALIZATION_PARAMETER = "localization"
private const val KEY_SCHEMA_VERSION = "schemaVersion"
private const val KEY_DEFAULT_LANGUAGE = "defaultLanguage"
private const val KEY_LANGUAGE_ROSTER = "languageRoster"
private const val SUPPORTED_SCHEMA_VERSION = 1
private val PLACEHOLDER_REGEX = Regex("""%([0-9]+)[$]([sd])""")

@Single
class LocalizationDataSource {

    private val json = Json { ignoreUnknownKeys = false }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getBundled(): LocalizationDocument {
        val jsonString = Res.readBytes(LOCALIZATION_RESOURCE_PATH).decodeToString()
        return parse(jsonString)
    }

    suspend fun getActivatedRemote(bundled: LocalizationDocument): LocalizationDocument? = try {
        val remoteJson = getLocalization()
        if (remoteJson != null) {
            parseRemote(remoteJson, bundled)
        } else {
            null
        }
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        recordException(error)
        null
    }

    suspend fun getLocalization(): String? {
        LocalizationPlatform.initializeRemoteConfig()
        return Firebase.remoteConfig.getString(LOCALIZATION_PARAMETER)?.takeIf(String::isNotBlank)
    }

    suspend fun fetchAndActivate() {
        LocalizationPlatform.initializeRemoteConfig()
        Firebase.remoteConfig.fetchAndActivate()
    }

    fun recordException(error: Throwable) {
        Firebase.crashlytics.recordException(error)
    }

    fun currentLanguageCode(): LanguageCode =
        LanguageCode(LocalizationPlatform.currentLanguageTag())

    fun parse(value: String): LocalizationDocument {
        val root = json.parseToJsonElement(value).jsonObject
        val schemaVersion = root.getValue(KEY_SCHEMA_VERSION).jsonPrimitive.content.toInt()
        val defaultLanguage = LanguageCode(root.getValue(KEY_DEFAULT_LANGUAGE).jsonPrimitive.content)
        val rawLanguageRoster = root.getValue(KEY_LANGUAGE_ROSTER).jsonObject

        val languageRoster = rawLanguageRoster.entries.associate { (language, rawStrings) ->
            val stringMap = rawStrings.jsonObject.toLocalizationStringMap(language)
            LanguageCode(language) to stringMap
        }

        val document = LocalizationDocument(
            schemaVersion = schemaVersion,
            defaultLanguage = defaultLanguage,
            languageRoster = languageRoster,
        )
        validate(document)
        return document
    }

    fun parseRemote(value: String, bundled: LocalizationDocument): LocalizationDocument {
        val remote = parse(value)
        val bundledDefaultStrings = bundled.languageRoster.getValue(bundled.defaultLanguage)
        val bundledKeyRoster = bundledDefaultStrings.keys

        require(remote.schemaVersion == bundled.schemaVersion) {
            "Remote localization schema does not match bundled localization"
        }
        require(remote.defaultLanguage == bundled.defaultLanguage) {
            "Remote localization default language does not match bundled localization"
        }
        require(remote.languageRoster.keys == bundled.languageRoster.keys) {
            "Remote localization language roster does not match bundled localization"
        }
        remote.languageRoster.forEach { (language, stringMap) ->
            val missingKeys = bundledKeyRoster - stringMap.keys
            val extraKeys = stringMap.keys - bundledKeyRoster
            require(stringMap.keys == bundledKeyRoster) {
                "Remote key mismatch for '${language.value}'. Missing: $missingKeys, Extra: $extraKeys"
            }
        }
        return remote
    }

    private fun validate(document: LocalizationDocument) {
        require(document.schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "Unsupported localization schema: ${document.schemaVersion}"
        }
        require(document.languageRoster.isNotEmpty()) {
            "Localization must contain at least one language"
        }
        val supportedLanguageRoster = LanguagePreference.entries
            .filterNot { it.isDefault }
            .map { it.languageCode }
            .toSet()
        require(document.languageRoster.keys == supportedLanguageRoster) {
            "Localization language roster does not match supported languages"
        }
        val defaultStrings = requireNotNull(document.languageRoster[document.defaultLanguage]) {
            "Localization must contain default language: ${document.defaultLanguage.value}"
        }
        require(defaultStrings.value.isNotEmpty()) {
            "Localization must contain strings"
        }
        document.languageRoster.forEach { (language, stringMap) ->
            require(stringMap.keys == defaultStrings.keys) {
                "Localization key mismatch for language: ${language.value}"
            }
            stringMap.value.forEach { (key, value) ->
                require(value.isNotEmpty()) { "Localization value is empty: ${language.value}/$key" }
                val defaultValue = defaultStrings.value.getValue(key)
                val defaultPlaceholderRoster = placeholders(defaultValue)
                val placeholderRoster = placeholders(value)
                require(placeholderRoster == defaultPlaceholderRoster) {
                    "Localization placeholder mismatch: ${language.value}/$key"
                }
            }
        }
    }

    private fun JsonObject.toLocalizationStringMap(language: String): LocalizationStringMap {
        val stringMap = mapValues { (key, value) ->
            val primitive = value.jsonPrimitive
            require(primitive.isString) { "Localization value must be a string: $language/$key" }
            primitive.content
        }
        return LocalizationStringMap(stringMap)
    }

    private fun placeholders(value: String): List<String> = PLACEHOLDER_REGEX
        .findAll(value)
        .map { it.value }
        .sorted()
        .toList()
}
