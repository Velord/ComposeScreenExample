package com.velord.buildlogic.model

import org.junit.Test
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private const val ANDROID_APP_BUILD_SCRIPT = "app/android/build.gradle.kts"
private const val RUNTIME_CONFIG_SOURCE = "infrastructure/config/src/commonMain/kotlin"
private val RUNTIME_ENVIRONMENT_REGEX = Regex("""enum class BuildEnvironment\s*\{([^;]+);""")
private val ENVIRONMENT_ENTRY_REGEX = Regex("""\b[A-Za-z]+\b""")

class BuildEnvironmentTest {

    private val appBuildScript = locateRepoRoot()
        .resolve(ANDROID_APP_BUILD_SCRIPT)
        .canonicalFile
        .readText()

    @Test
    fun `non production environments use their own application ID suffix`() {
        BuildEnvironment.entries
            .filterNot { environment -> environment.isProduction }
            .forEach { environment ->
                val environmentBlock = appBuildScript.environmentBlock(environment)
                val suffixEnvironment = if (environment.isQa) {
                    BuildEnvironment.Develop
                } else {
                    environment
                }
                val environmentName = suffixEnvironment.name
                val expectedSuffix = "applicationIdSuffix = " +
                    "\".\${BuildEnvironment.$environmentName.value}\""

                assertContains(environmentBlock, expectedSuffix)
            }
    }

    @Test
    fun `production environment has no application ID suffix`() {
        val environmentBlock = appBuildScript.environmentBlock(BuildEnvironment.Production)

        assertFalse(environmentBlock.contains("applicationIdSuffix"))
    }

    @Test
    fun `runtime environment names match the build variant environment`() {
        val runtimeSource = locateRepoRoot()
            .resolve(RUNTIME_CONFIG_SOURCE)
            .walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .map { it.readText() }
            .firstNotNullOf { RUNTIME_ENVIRONMENT_REGEX.find(it) }
        val runtimeNameRoster = ENVIRONMENT_ENTRY_REGEX
            .findAll(runtimeSource.groupValues[1])
            .map { it.value }
            .toList()

        assertEquals(BuildEnvironment.entries.map { it.name }, runtimeNameRoster)
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
        File(currentDirectory, ANDROID_APP_BUILD_SCRIPT).isFile
} ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
