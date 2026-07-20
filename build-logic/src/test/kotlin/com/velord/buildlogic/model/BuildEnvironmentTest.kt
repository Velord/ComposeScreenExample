package com.velord.buildlogic.model

import org.junit.Test
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertFalse

class BuildEnvironmentTest {

    private val appBuildScript = locateRepoRoot()
        .resolve("app/build.gradle.kts")
        .canonicalFile
        .readText()

    @Test
    fun `non production environments use their own application ID suffix`() {
        BuildEnvironment.entries
            .filterNot { environment -> environment == BuildEnvironment.Production }
            .forEach { environment ->
                val environmentBlock = appBuildScript.environmentBlock(environment)
                val expectedSuffix =
                    "applicationIdSuffix = \".\${BuildEnvironment.${environment.name}.value}\""

                assertContains(environmentBlock, expectedSuffix)
            }
    }

    @Test
    fun `production environment has no application ID suffix`() {
        val environmentBlock = appBuildScript.environmentBlock(BuildEnvironment.Production)

        assertFalse(environmentBlock.contains("applicationIdSuffix"))
    }
}

private fun String.environmentBlock(environment: BuildEnvironment): String {
    val blockStart = "create(BuildEnvironment.${environment.name}.value) {"
    val environmentBlock = substringAfter(blockStart, missingDelimiterValue = "")

    require(environmentBlock.isNotEmpty()) { "Missing product flavor for ${environment.name}" }
    return environmentBlock.substringBefore("\n        }")
}

private fun locateRepoRoot(): File = generateSequence(
    File(System.getProperty("user.dir")).absoluteFile,
) { currentDirectory ->
    currentDirectory.parentFile
}.firstOrNull { currentDirectory ->
    File(currentDirectory, "settings.gradle.kts").isFile &&
        File(currentDirectory, "app/build.gradle.kts").isFile
} ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
