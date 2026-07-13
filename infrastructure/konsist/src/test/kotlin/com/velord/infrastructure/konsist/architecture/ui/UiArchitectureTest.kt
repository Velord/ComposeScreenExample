package com.velord.infrastructure.konsist.architecture.ui

import com.lemonappdev.konsist.api.Konsist
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val SCREEN_FILE_SUFFIX = "Screen.kt"
private const val LIFECYCLE_COLLECTION = "collectAsStateWithLifecycle"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private const val UI_PACKAGE_PREFIX = "com.velord.ui."
private const val ANDROID_TOAST_TYPE = "android.widget.Toast"
private const val TOAST_CONFIG_TYPE = "ToastConfig"
private val HARDCODED_MESSAGE_REGEX =
    Regex("""(?:\bval\s+\w*[Mm]essage\w*\s*=\s*|message\s*=\s*)"[^"]+"""")
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

    @Test
    fun `ui should not use Android Toast directly`() {
        val violationRoster = projectFileRoster.filter { file ->
            file.packagee?.name?.startsWith(UI_PACKAGE_PREFIX) == true &&
                file.text.lines().any(::containsAndroidToastUsage)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "UI must emit toast through ShowToastUC: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `toast messages should come from resources`() {
        val violationRoster = projectFileRoster.filter { file ->
            file.packagee?.name?.startsWith(UI_PACKAGE_PREFIX) == true &&
                file.text.contains(TOAST_CONFIG_TYPE) &&
                HARDCODED_MESSAGE_REGEX.containsMatchIn(file.text)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Toast messages must come from resources: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    private fun screenFileRoster() = projectFileRoster.filter { file ->
        file.name.endsWith(SCREEN_FILE_SUFFIX)
    }

    private fun isLifecycleCollection(line: String): Boolean {
        val lineTrimmed = line.trimStart()
        return lineTrimmed.startsWith("import ").not() &&
            line.contains(LIFECYCLE_COLLECTION)
    }

    private fun containsAndroidToastUsage(line: String): Boolean {
        val code = line.substringBefore("//")
        return code.contains(ANDROID_TOAST_TYPE)
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
}
