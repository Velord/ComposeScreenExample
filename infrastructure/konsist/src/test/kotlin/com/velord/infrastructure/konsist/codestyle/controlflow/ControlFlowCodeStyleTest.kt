package com.velord.infrastructure.konsist.codestyle.controlflow

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import kotlin.test.Test

class ControlFlowCodeStyleTest {
    @Test
    fun `if statements should keep opening condition on the same line when it fits`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitIfOpeningCondition(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split if opening condition at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `guard return series should be followed by one blank line before final return`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isMissingBlankLineAfterGuardReturns(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Missing blank line after guard returns at line ${violation + 1}."
                )
            }

            violation == null
        }
    }

    @Test
    fun `when branches should not be separated by blank lines`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (1 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineBetweenWhenBranches(
                    previousLine = lineRoster[lineIndex - 1],
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "When branches must not be separated by blank line ${violation + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single expression when branches should not use braces`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 2)).firstOrNull { lineIndex ->
                isSingleExpressionWhenBranchWithBraces(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single expression when branch at line ${violation + 1} " +
                    "should not use braces."
                println(msg)
            }

            violation == null
        }
    }

}
