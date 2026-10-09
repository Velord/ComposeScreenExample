package com.velord.infrastructure.konsist.codestyle.controlflow

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import com.velord.infrastructure.konsist.codestyle.withKotlinFileRoster
import kotlin.test.Test
import kotlin.test.assertEquals

class ControlFlowCodeStyleTest {

    @Test
    fun `fixed enum entry comparisons should use enum predicates`() {
        projectFileRoster.map { it.text }.withKotlinFileRoster { kotlinFileRoster ->
            val rule = EnumEntryCodeStyleRule(kotlinFileRoster)
            val violationRoster = projectFileRoster.flatMapIndexed { index, file ->
                rule.violationRoster(index).map { "${file.path}: $it" }
            }
            assertEquals(emptyList(), violationRoster)
        }
    }

    @Test
    fun `assigned multiline if else expressions should brace both branch bodies`() {
        projectFileRoster.map { it.text }.withKotlinFileRoster { kotlinFileRoster ->
            val violationRoster = projectFileRoster.flatMapIndexed { index, file ->
                kotlinFileRoster[index].unbracedIfElseLineRoster().map { line ->
                    "${file.path}:$line"
                }
            }
            assertEquals(emptyList(), violationRoster)
        }
    }

    @Test
    fun `multiline if else bracing is limited to assigned values`() {
        val samples = listOf(
            "fun f(flag: Boolean) { val x = if (flag) 1\nelse 2 }",
            "fun f(flag: Boolean) { var x = if (flag) 1\nelse 2 }",
            "fun f(flag: Boolean) { var x = 0; x = if (flag) 1\nelse 2 }",
            "val x: Int get() = if (true) 1\nelse 2",
            "fun f(flag: Boolean) { val x = if (flag) { 1 }\nelse 2 }",
            "fun f(flag: Boolean) { if (flag) println(1)\nelse println(2) }",
            "fun f(flag: Boolean): Int { return if (flag) 1\nelse 2 }",
            "fun f(flag: Boolean) = if (flag) 1\nelse 2",
            "fun f(flag: Boolean) { val x = if (flag) {\nif (flag) 1\nelse 2\n} else { 3 } }",
            "fun f(flag: Boolean) { val x = if (flag) 1 else 2 }",
        )
        samples.withKotlinFileRoster { kotlinFileRoster ->
            assertEquals(
                listOf(1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
                kotlinFileRoster.map { it.unbracedIfElseLineRoster().size },
            )
        }
    }

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
        projectFileRoster.map { it.text }.withKotlinFileRoster { kotlinFileRoster ->
            val branchLineRosterByFile = kotlinFileRoster.associate { file ->
                file.text to file.whenBranchLineRoster()
            }
            projectFileRoster.assertTrue { file ->
                val lineRoster = file.text.lines()
                val branchLineRoster = branchLineRosterByFile.getValue(file.text)
                val violation = branchLineRoster.firstOrNull { lineIndex ->
                    isSingleExpressionWhenBranchWithBraces(
                        currentLine = lineRoster[lineIndex],
                        nextLine = lineRoster.getOrNull(lineIndex + 1).orEmpty(),
                        thirdLine = lineRoster.getOrNull(lineIndex + 2).orEmpty(),
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

}
