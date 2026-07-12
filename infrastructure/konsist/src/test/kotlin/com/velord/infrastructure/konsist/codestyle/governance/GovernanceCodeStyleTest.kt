package com.velord.infrastructure.konsist.codestyle.governance

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import kotlin.test.Test

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

}
