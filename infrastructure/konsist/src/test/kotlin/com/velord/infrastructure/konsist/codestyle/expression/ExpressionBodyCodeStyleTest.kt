package com.velord.infrastructure.konsist.codestyle.expression

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import kotlin.test.Test

class ExpressionBodyCodeStyleTest {
    @Test
    fun `expression-bodied when declarations should keep = and when on the same line`() {
        projectFileRoster.assertTrue { file ->
            val violation = file.text.lines()
                .zipWithNext()
                .withIndex()
                .firstOrNull { (_, linePair) ->
                    linePair.first.trimEnd().endsWith("=") &&
                        linePair.second.trimStart().startsWith("when {")
                }

            if (violation != null) {
                val lineNumber = violation.index + 1
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split '= when' declaration at line $lineNumber."
                )
            }

            violation == null
        }
    }

    @Test
    fun `expression-bodied declarations should keep = call on same line when it fits`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitExpressionBodyOpeningCall(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split '= call' declaration at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `expression-bodied functions should split parameters before wrapping opening call after =`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isInlineParameterFunctionWithWrappedOpeningCall(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Inline function parameters at line ${violation + 1} " +
                    "force the opening call after = onto a broken indentation level."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `expression-bodied declarations should not put multiline calls after operators`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = lineRoster.indexOfFirst { line ->
                isExpressionBodyOperatorBeforeMultilineCall(line)
            }.takeIf { index -> index >= 0 }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Expression body at line ${violation + 1} puts a " +
                    "multiline call after an operator."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `block-bodied property getter openings should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitBlockBodyPropertyGetterOpening(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Block-bodied property getter opening at " +
                    "line ${violation + 1} should stay on one line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single return functions should use expression bodies when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSingleReturnBlockFunction(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster.getOrNull(lineIndex + 1).orEmpty(),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single return function at " +
                    "line ${violation + 1} should use an expression body."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `declarations should not wrap rhs when two-line form fits within 100 chars`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isShortWrappedDeclaration(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Short wrapped declaration at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `short elvis expressions should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitShortElvisExpression(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Short Elvis expression at line ${violation + 1} should stay on one line."
                println(msg)
            }

            violation == null
        }
    }
}
