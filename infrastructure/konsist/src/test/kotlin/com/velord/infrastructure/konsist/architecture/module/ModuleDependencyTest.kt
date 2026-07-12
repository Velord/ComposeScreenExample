package com.velord.infrastructure.konsist.architecture.module

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val UI_FOLDER_NAME = "ui"
private const val BUILD_FILE_NAME = "build.gradle.kts"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private val DATA_PROJECT_ACCESSOR_REGEX = Regex("""projects\.data\.[A-Za-z][A-Za-z0-9]*""")
private val DATA_PROJECT_PATH_REGEX = Regex("""project\(\s*[\"']:data:[^\"']+[\"']\s*\)""")

class ModuleDependencyTest {

    private val repoRoot = locateRepoRoot()

    @Test
    fun `ui modules should not depend directly on data modules`() {
        val violationRoster = File(repoRoot, UI_FOLDER_NAME)
            .walkTopDown()
            .filter { file -> file.name == BUILD_FILE_NAME }
            .flatMap { file ->
                file.readLines().withIndex().mapNotNull { (lineIndex, line) ->
                    line.takeIf(::isDirectDataDependency)?.let {
                        "${file.relativeTo(repoRoot).path}:${lineIndex + 1}"
                    }
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "UI modules depend directly on data modules: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    private fun isDirectDataDependency(line: String): Boolean {
        if (line.trimStart().startsWith("//")) return false

        return DATA_PROJECT_ACCESSOR_REGEX.containsMatchIn(line) ||
            DATA_PROJECT_PATH_REGEX.containsMatchIn(line)
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
}
