package com.velord.infrastructure.konsist.codestyle.compose

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.call.composeCallNameRoster
import com.velord.infrastructure.konsist.codestyle.call.isCompactComposeCallWithSeveralParameters
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
private val DELEGATED_REMEMBERED_MUTABLE_STATE_REGEX = Regex(
    """\bvar\s+\w+\s+by\s+remember(?:Saveable)?\s*""" +
        """(?:\([^)]*\))?\s*\{\s*mutable(?:Int|Long|Float|Double)?StateOf\(""",
)
private val EMPTY_MODIFIER_ARGUMENT_REGEX = Regex("""(?m)^\s*modifier\s*=\s*Modifier\s*,\s*$""")

class ComposeCodeStyleTest {

    private val projectFileRoster = Konsist.scopeFromExternalDirectory(locateRepoRoot().path).files
    private val projectComposeCallNameRoster = projectFileRoster
        .flatMap { file -> composeCallNameRoster(file.text) }
        .toSet()

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

    @Test
    fun `remembered mutable state should use State suffixed value holders`() {
        projectFileRoster.assertTrue { file ->
            val violation = DELEGATED_REMEMBERED_MUTABLE_STATE_REGEX.find(file.text)

            if (violation != null) {
                val lineNumber = file.text.take(violation.range.first).count { it == '\n' } + 1
                val msg = "Name: ${file.name}. FAILED. " +
                    "Delegated remembered mutable state at line $lineNumber."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compose calls with several parameters should use one parameter per line`() {
        projectFileRoster.assertTrue { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                isCompactComposeCallWithSeveralParameters(projectComposeCallNameRoster, line)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compose call at line ${violation.index + 1} " +
                    "should use one parameter per line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `empty Modifier arguments should be omitted`() {
        projectFileRoster.assertTrue { file ->
            val violation = EMPTY_MODIFIER_ARGUMENT_REGEX.find(file.text)

            if (violation != null) {
                val lineNumber = file.text.take(violation.range.first).count { it == '\n' } + 1
                val msg = "Name: ${file.name}. FAILED. " +
                    "Empty Modifier argument at line $lineNumber should be omitted."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `default Modifier parameters should be used by callers`() {
        val sourceRoster = projectFileRoster.map { file ->
            ModifierSource(
                name = file.name,
                packageName = file.packagee?.name.orEmpty(),
                text = file.text,
            )
        }
        val violation = findUnnecessaryDefaultModifier(sourceRoster)

        if (violation != null) {
            val msg = "Name: ${violation.sourceName}. FAILED. " +
                "${violation.functionName} has an unused default Modifier parameter."
            println(msg)
        }

        assertTrue(violation == null)
    }

}
