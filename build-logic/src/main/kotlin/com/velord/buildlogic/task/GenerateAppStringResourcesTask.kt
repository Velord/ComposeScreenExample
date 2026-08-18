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
private const val DEFAULT_LANGUAGE = "en"
private val APP_STRING_KEY_REGEX = Regex("[A-Za-z_][A-Za-z0-9_]*")
private val FORMAT_PLACEHOLDER_REGEX = Regex("""%\d+\$[sd]""")

abstract class GenerateAppStringResourcesTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val localizationFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val document = JsonSlurper().parse(localizationFile.get().asFile) as Map<*, *>
        validate(document)

        val languages = document["languages"] as Map<*, *>
        val defaultStrings = languages[DEFAULT_LANGUAGE] as Map<*, *>
        val outputFile = outputDirectory.get().asFile.resolve(
            "com/velord/core/resource/AppString.kt",
        )
        val entryRoster = defaultStrings.entries
            .map { it.key.toString() to it.value.toString() }
            .sortedBy { it.first }

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
                entryRoster.forEach { (key, _) ->
                    append("    val $key = AppStringResource(")
                    append(key.toKotlinStringLiteral())
                    appendLine(")")
                }
                appendLine("}")
                appendLine()
                appendLine("internal val defaultLocalizationStringRoster = mapOf(")
                entryRoster.forEach { (key, value) ->
                    append("    ")
                    append(key.toKotlinStringLiteral())
                    append(" to ")
                    append(value.toKotlinStringLiteral())
                    appendLine(",")
                }
                appendLine(")")
            },
        )
    }

    private fun validate(document: Map<*, *>) {
        val schemaVersion = (document["schemaVersion"] as? Number)?.toInt()
        require(schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "localization.json must use schemaVersion $SUPPORTED_SCHEMA_VERSION"
        }

        val languages = document["languages"] as? Map<*, *>
            ?: error("localization.json must contain languages")
        require(languages.isNotEmpty()) {
            "localization.json must contain at least one language"
        }

        val defaultStrings = languages[DEFAULT_LANGUAGE].asStringMap(
            "languages.$DEFAULT_LANGUAGE",
        )
        require(defaultStrings.isNotEmpty()) {
            "localization.json must contain strings"
        }

        defaultStrings.keys.forEach { key ->
            require(APP_STRING_KEY_REGEX.matches(key)) {
                "Invalid AppString key: $key"
            }
        }

        languages.forEach { (languageKey, rawStrings) ->
            val language = languageKey.toString()
            val strings = rawStrings.asStringMap("languages.$language")
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

    private fun Any?.asStringMap(name: String): Map<String, String> {
        val value = this as? Map<*, *>
            ?: error("localization.json must contain $name")
        return value.entries.associate { (key, entryValue) ->
            val stringKey = key as? String
                ?: error("Localization key in $name must be a string")
            val stringValue = entryValue as? String
                ?: error("Localization value for $name/$stringKey must be a string")
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
