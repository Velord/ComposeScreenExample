package com.velord.infrastructure.konsist.codestyle.gradle

import com.velord.infrastructure.konsist.codestyle.locateRepoRoot
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class GradleCodeStyleTest {
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

}
