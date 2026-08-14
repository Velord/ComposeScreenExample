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

abstract class GenerateAppStringResourcesTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val localizationFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val document = JsonSlurper().parse(localizationFile.get().asFile) as Map<*, *>
        val languages = document["languages"] as? Map<*, *>
            ?: error("localization.json must contain languages")
        val english = languages["en"] as? Map<*, *>
            ?: error("localization.json must contain languages.en")
        val outputFile = outputDirectory.get().asFile.resolve(
            "com/velord/core/resource/AppString.kt",
        )
        val entryRoster = english.entries
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

    private fun String.toKotlinStringLiteral(): String = JsonOutput
        .toJson(this)
        .replace("$", "\\$")
}
