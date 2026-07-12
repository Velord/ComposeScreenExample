package com.velord.infrastructure.konsist.architecture.ui

import com.lemonappdev.konsist.api.Konsist
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val SCREEN_FILE_SUFFIX = "Screen.kt"
private const val LIFECYCLE_COLLECTION = "collectAsStateWithLifecycle"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private val DIRECT_VIEW_MODEL_COLLECTION_REGEX =
    Regex("""\bviewModel\.[A-Za-z][A-Za-z0-9]*\.collectAsStateWithLifecycle\(""")

class UiArchitectureTest {

    private val projectFileRoster = Konsist.scopeFromExternalDirectory(locateRepoRoot().path).files

    @Test
    fun `screen lifecycle state collection should be direct from view model`() {
        screenFileRoster().forEach { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                isLifecycleCollection(line) &&
                    DIRECT_VIEW_MODEL_COLLECTION_REGEX.containsMatchIn(line).not()
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Lifecycle state collection at line ${violation.index + 1} " +
                    "must be direct from viewModel."
                println(msg)
            }

            assertTrue(violation == null)
        }
    }

    private fun screenFileRoster() = projectFileRoster.filter { file ->
        file.name.endsWith(SCREEN_FILE_SUFFIX)
    }

    private fun isLifecycleCollection(line: String): Boolean {
        val lineTrimmed = line.trimStart()
        return lineTrimmed.startsWith("import ").not() &&
            line.contains(LIFECYCLE_COLLECTION)
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
}
