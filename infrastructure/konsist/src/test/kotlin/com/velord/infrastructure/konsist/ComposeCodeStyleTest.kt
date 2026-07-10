package com.velord.infrastructure.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val SCREEN_FILE_SUFFIX = "Screen.kt"
private const val COMMON_MAIN_PATH = "/src/commonMain/"
private const val SOURCE_PATH = "/src/"
private const val PREVIEW_ANNOTATION = "@Preview"
private const val PREVIEW_COMBINED_ANNOTATION = "@PreviewCombined"
private const val COMPOSABLE_ANNOTATION = "@Composable"
private const val PREVIEW_FUNCTION = "private fun Preview() {"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"

class ComposeCodeStyleTest {

    private val projectFileRoster = Konsist.scopeFromExternalDirectory(locateRepoRoot().path).files

    @Test
    fun `compose screen roster should not be empty`() {
        assertTrue(screenFileRoster().isNotEmpty())
    }

    @Test
    fun `screen preview declarations should use project preview shape`() {
        screenFileRoster().assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = lineRoster.withIndex().firstOrNull { (lineIndex, line) ->
                isPreviewAnnotation(line) &&
                    isProjectPreviewShape(
                        firstLine = line,
                        secondLine = lineRoster.getOrNull(lineIndex + 1),
                        thirdLine = lineRoster.getOrNull(lineIndex + 2),
                    ).not()
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Screen preview at line ${violation.index + 1} must use project shape."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `common screen previews should use PreviewCombined`() {
        screenFileRoster()
            .filter { file -> file.path.invariantPath().contains(COMMON_MAIN_PATH) }
            .assertTrue { file ->
                val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                    line.trim() == PREVIEW_ANNOTATION
                }

                if (violation != null) {
                    val msg = "Name: ${file.name}. FAILED. " +
                        "Common screen preview at line ${violation.index + 1} " +
                        "must use PreviewCombined."
                    println(msg)
                }

                violation == null
            }
    }

    private fun screenFileRoster() = projectFileRoster.filter { file ->
        val path = file.path.invariantPath()
        path.endsWith(SCREEN_FILE_SUFFIX) && path.contains(SOURCE_PATH)
    }

    private fun isProjectPreviewShape(
        firstLine: String,
        secondLine: String?,
        thirdLine: String?,
    ): Boolean = isPreviewAnnotation(firstLine) &&
        secondLine?.trim() == COMPOSABLE_ANNOTATION &&
        thirdLine?.trim() == PREVIEW_FUNCTION

    private fun isPreviewAnnotation(line: String): Boolean {
        val lineTrimmed = line.trim()
        return lineTrimmed == PREVIEW_ANNOTATION ||
            lineTrimmed == PREVIEW_COMBINED_ANNOTATION
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")

    private fun String.invariantPath(): String = replace(File.separatorChar, '/')
}
