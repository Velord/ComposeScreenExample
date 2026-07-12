package com.velord.infrastructure.konsist.codestyle

import com.lemonappdev.konsist.api.Konsist
import java.io.File

private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private val WHITESPACE_REGEX = Regex("\\s+")

internal const val HARD_WRAP = 100
internal val projectFileRoster = Konsist.scopeFromProject().files

internal fun joinLine(currentLine: String, nextLine: String): String = compactWhitespace(
    "${currentLine.trimEnd()} ${nextLine.trimStart()}",
)

internal fun compactWhitespace(value: String): String = value
    .replace(WHITESPACE_REGEX, " ")
    .trim()

internal fun locateRepoRoot(): File = generateSequence(
    File(System.getProperty("user.dir")).absoluteFile,
) { currentDirectory ->
    currentDirectory.parentFile
}.firstOrNull { currentDirectory ->
    File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
} ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
