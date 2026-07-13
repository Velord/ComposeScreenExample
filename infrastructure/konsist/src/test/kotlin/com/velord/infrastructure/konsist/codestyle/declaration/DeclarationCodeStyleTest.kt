package com.velord.infrastructure.konsist.codestyle.declaration

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.koscope.declarationsOf
import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import kotlin.test.Test
import kotlin.test.assertTrue

private const val VIEW_MODEL_SUFFIX = "VM"
private val VIEW_MODEL_PARENT_NAME_ROSTER = setOf("ViewModel", "CoroutineScopeVM")

class DeclarationCodeStyleTest {

    private val projectScope = Konsist.scopeFromProject()
    @Test
    fun `function bodies should not start with a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterFunctionOpeningBrace(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Function body starts with a blank line at line ${violation + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single parameter class constructors should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitSingleParameterClassHeader(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Split single-parameter class header at " +
                    "line ${violation + 1} exceeds style rules."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers should be followed by one blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isMissingBlankLineAfterCompactClassHeader(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Missing blank line after compact class header at line ${violation + 1}."
                )
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers before single one-line members should not have blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 2)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactClassHeaderBeforeOneLineMember(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact class header at line ${violation + 1} " +
                    "must not have a blank line before first one-line member."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers before non one-line members should keep a blank line`() {
        val lineRoster = listOf(
            "class ToastGateway(private val appState: AppStateDataSource) {",
            "    fun getFlow(): Flow<ToastConfig> = appState.toastConfigFlow",
            "",
            "    suspend fun show(config: ToastConfig) {",
        )

        val isViolation = isMissingBlankLineAfterCompactClassHeader(
            lineRoster = lineRoster,
            lineIndex = 0,
            currentLine = lineRoster[0],
            nextLine = lineRoster[1],
        )

        assertTrue(isViolation)
    }

    @Test
    fun `compact abstract class headers before abstract member should not be followed by a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactAbstractClassHeaderBeforeAbstractMember(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact abstract class header at line ${violation + 1} " +
                    "must not have a blank line before first abstract member."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact sealed headers without parent functions should not be followed by a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineAfterCompactSealedHeaderWithoutParentFunction(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact sealed header at line ${violation + 1} " +
                    "must not have a blank line when parent has no functions."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact enum headers should not be followed by a blank line before first entry`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactEnumHeader(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact enum header at line ${violation + 1} " +
                    "must not have a blank line before first entry."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `companion objects should not start with a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineAfterCompanionObjectOpening(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Companion object at line ${violation + 1} " +
                    "must not start with a blank line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `companion default value declarations should use DEFAULT name`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isCompanionDefaultValueDeclaration(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Default value at line ${violation + 1} must be named DEFAULT."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `view model classes and files should use VM suffix`() {
        projectScope
            .declarationsOf<KoClassDeclaration>()
            .filter { declaration ->
                declaration.hasParentWithName(
                    names = VIEW_MODEL_PARENT_NAME_ROSTER,
                    indirectParents = true,
                )
            }
            .assertTrue { declaration ->
                declaration.name.endsWith(VIEW_MODEL_SUFFIX) &&
                    declaration.containingFile.name == declaration.name
            }
    }
}
