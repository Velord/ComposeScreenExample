package com.velord.buildlogic.task

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

private const val SUPPORTED_SCHEMA_VERSION = 1
private val APP_STRING_KEY_REGEX = Regex("[A-Za-z_][A-Za-z0-9_]*")
private val FORMAT_PLACEHOLDER_REGEX = Regex("""%[0-9]+[$][sd]""")

abstract class GenerateAppStringResourcesTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val localizationFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val document = JsonSlurper().parse(localizationFile.get().asFile) as Map<*, *>
        val languageRoster = validate(document)
        val defaultLanguage = resolveDefaultLanguage(document, languageRoster)
        val defaultStringMap = languageRoster.getValue(defaultLanguage)
        val keyRoster = defaultStringMap.keys.sorted()
        val outputFile = outputDirectory.get().asFile.resolve("com/velord/core/resource/AppString.kt")

        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            buildString {
                appendLine("package com.velord.core.resource")
                appendLine()
                appendLine("@JvmInline")
                appendLine("value class AppStringResource internal constructor(")
                appendLine("    internal val key: String,")
                appendLine(")")
                appendLine()
                appendLine("object AppString {")
                keyRoster.forEach { key ->
                    append("    val $key = AppStringResource(")
                    append(key.toKotlinStringLiteral())
                    appendLine(")")
                }
                appendLine("}")
            },
        )
    }

    private fun validate(document: Map<*, *>): Map<String, Map<String, String>> {
        val schemaVersion = (document["schemaVersion"] as? Number)?.toInt()
        require(schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "localization.json must use schemaVersion $SUPPORTED_SCHEMA_VERSION"
        }

        val rawLanguageRoster = document["languageRoster"] as? Map<*, *>
            ?: error("localization.json must contain languageRoster")
        require(rawLanguageRoster.isNotEmpty()) {
            "localization.json must contain at least one language"
        }

        val languageRoster = rawLanguageRoster.entries.associate { (languageKey, rawStrings) ->
            val invalidLanguageMessage = "Localization language code must be a string"
            val language = languageKey as? String ?: error(invalidLanguageMessage)
            val stringMap = rawStrings.asStringMap("languageRoster.$language")

            language to stringMap
        }
        val defaultLanguage = resolveDefaultLanguage(document, languageRoster)
        val defaultStringMap = languageRoster.getValue(defaultLanguage)
        require(defaultStringMap.isNotEmpty()) {
            "localization.json must contain strings"
        }

        defaultStringMap.keys.forEach { key ->
            require(APP_STRING_KEY_REGEX.matches(key)) {
                "Invalid AppString key: $key"
            }
        }

        languageRoster.forEach { (language, stringMap) ->
            require(stringMap.keys == defaultStringMap.keys) {
                "Localization key mismatch for language: $language"
            }
            stringMap.forEach { (key, value) ->
                require(value.isNotEmpty()) {
                    "Localization value is empty: $language/$key"
                }

                val defaultValue = defaultStringMap.getValue(key)
                val defaultPlaceholderRoster = placeholders(defaultValue)
                val placeholderRoster = placeholders(value)
                require(placeholderRoster == defaultPlaceholderRoster) {
                    "Localization placeholder mismatch: $language/$key"
                }
            }
        }

        return languageRoster
    }

    private fun resolveDefaultLanguage(
        document: Map<*, *>,
        languageRoster: Map<String, Map<String, String>>,
    ): String {
        val defaultLanguage = document["defaultLanguage"] as? String
            ?: error("localization.json must contain defaultLanguage")
        require(defaultLanguage.isNotBlank()) {
            "localization.json defaultLanguage must not be blank"
        }
        require(defaultLanguage in languageRoster) {
            "Default localization language '$defaultLanguage' is missing"
        }

        return defaultLanguage
    }

    private fun Any?.asStringMap(name: String): Map<String, String> {
        val value = this as? Map<*, *> ?: error("localization.json must contain $name")
        return value.entries.associate { (key, entryValue) ->
            val invalidKeyMessage = "Localization key in $name must be a string"
            val invalidValueMessage = "Localization value for $name must be a string"
            val stringKey = key as? String ?: error(invalidKeyMessage)
            val stringValue = entryValue as? String ?: error(invalidValueMessage)

            stringKey to stringValue
        }
    }

    private fun placeholders(value: String): List<String> = FORMAT_PLACEHOLDER_REGEX
        .findAll(value)
        .map { it.value }
        .sorted()
        .toList()

    private fun String.toKotlinStringLiteral(): String = JsonOutput
        .toJson(this)
        .replace("$", "\\$")
}
