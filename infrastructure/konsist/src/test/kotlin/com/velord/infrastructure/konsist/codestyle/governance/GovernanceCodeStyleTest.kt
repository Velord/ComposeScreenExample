package com.velord.infrastructure.konsist.codestyle.governance

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.locateRepoRoot
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val KONSIST_TEST_PATH = "infrastructure/konsist/src/test"
private const val TEST_ANNOTATION = "@Test"

class GovernanceCodeStyleTest {
    @Test
    fun `governed kotlin files should not exceed hard wrap`() {
        hardWrapFileRoster.assertTrue { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                line.length > HARD_WRAP &&
                    isAllowedHardWrapTestName(line).not()
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Hard wrap exceeded at line ${violation.index + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `governance const declarations should stay top level after imports`() {
        governanceFileRoster.assertTrue { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (lineIndex, _) ->
                isLateConstDeclaration(lineRoster = file.text.lines(), lineIndex = lineIndex)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Const declaration at line ${violation.index + 1} " +
                    "must stay top level after imports."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `gradle test dependencies should use version catalog aliases`() {
        val violation = locateRepoRoot()
            .walkTopDown()
            .filter { file -> file.isFile && file.name.endsWith(".gradle.kts") }
            .filter { file -> file.path.contains(
                other = "${File.separator}build${File.separator}").not()
            }
            .firstOrNull { file -> file.readText().contains("kotlin(\"test\")") }

        if (violation != null) {
            val msg = "Name: ${violation.name}. FAILED. " +
                "Use libs.kotlin.test instead of kotlin(\"test\")."
            println(msg)
        }

        assertTrue(violation == null)
    }

    @Test
    fun `konsist test classes should contain several related tests`() {
        val violationRoster = File(locateRepoRoot(), KONSIST_TEST_PATH)
            .walkTopDown()
            .filter { file -> file.isFile && file.name.endsWith("Test.kt") }
            .filter { file ->
                file.readLines().count { line -> line.trim() == TEST_ANNOTATION } == 1
            }
            .map { file -> file.name }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "Single-test Konsist classes: " + violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }
}
