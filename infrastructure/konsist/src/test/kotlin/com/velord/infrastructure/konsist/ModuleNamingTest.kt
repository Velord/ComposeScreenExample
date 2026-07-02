package com.velord.infrastructure.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class ModuleNamingTest {

    private val repoRoot = locateRepoRoot()

    @Test
    fun `kotlin file should live under an allowed module naming family`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { file ->
                val modulePath = modulePathOrNull(file.path)
                modulePath != null && file.path.contains("${File.separator}src${File.separator}")
            }
            .assertTrue { file ->
                val modulePath = modulePathOrNull(file.path)
                val isValid = modulePath != null && modulePath.matchesNamingFamily()

                if (isValid.not()) {
                    println(
                        "Name: ${file.name}. FAILED. Module path '$modulePath' does not match the allowed naming families."
                    )
                }

                isValid
            }
    }

    @Test
    fun `kotlin file package root should match its module path`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { file ->
                val modulePath = modulePathOrNull(file.path)
                val packageName = file.packagee?.name
                modulePath != null && packageName != null && file.path.contains("${File.separator}src${File.separator}")
            }
            .assertTrue { file ->
                val modulePath = modulePathOrNull(file.path) ?: return@assertTrue false
                val packageName = file.packagee?.name ?: return@assertTrue false
                val expectedPackageRoot = modulePath.expectedPackageRoot()
                val isValid = expectedPackageRoot?.let { packageName == it || packageName.startsWith("$it.") } == true

                if (isValid.not()) {
                    println(
                        "Name: ${file.name}. FAILED. Package '$packageName' does not match module '$modulePath'. " +
                            "Expected root: '$expectedPackageRoot'."
                    )
                }

                isValid
            }
    }

    private fun locateRepoRoot(): File =
        generateSequence(File(System.getProperty("user.dir")).absoluteFile) { currentDirectory ->
            currentDirectory.parentFile
        }.firstOrNull { currentDirectory ->
            File(currentDirectory, "settings.gradle.kts").isFile
        } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")

    private fun modulePathOrNull(filePath: String): ModulePath? {
        val relativePath = File(filePath).absoluteFile
            .relativeTo(repoRoot)
            .invariantSeparatorsPath

        val pathSegmentRoster = relativePath.split('/')
        if (pathSegmentRoster.isEmpty()) return null

        if (pathSegmentRoster.first() == "build-logic") {
            return ModulePath("build-logic")
        }

        val srcIndex = pathSegmentRoster.indexOf("src")
        if (srcIndex <= 0) return null

        val moduleSegmentRoster = pathSegmentRoster.take(srcIndex)
        return ModulePath(":" + moduleSegmentRoster.joinToString(":"))
    }
}

private data class ModulePath(
    val value: String,
) {
    private val segmentRoster = value.removePrefix(":").split(":")
    private val leafName = segmentRoster.last()

    fun matchesNamingFamily(): Boolean =
        when {
            value == "build-logic" -> true
            value == ":app" -> true
            value == ":model" -> true
            value == ":ui:sharedviewmodel" -> true
            value.startsWith(":domain:") -> segmentRoster.size == 2 && leafName.matches(usecaseRegex)
            value.startsWith(":data:") -> segmentRoster.size == 2 && leafName.matches(lowerCaseLeafRegex)
            value.startsWith(":core:") -> segmentRoster.size == 2 && leafName.matches(coreRegex)
            value.startsWith(":infrastructure:") -> segmentRoster.size == 2 && leafName.matches(lowerCaseLeafRegex)
            value.startsWith(":ui:") -> segmentRoster.size == 2 && (
                leafName.matches(featureRegex) || leafName == sharedViewModelName || leafName.matches(widgetRegex)
            )
            else -> false
        }

    fun expectedPackageRoot(): String? = when {
            value == "build-logic" -> "com.velord.buildlogic"
            value == ":app" -> "com.velord.composescreenexample"
            value == ":model" -> "com.velord.model"
            value == ":ui:sharedviewmodel" -> "com.velord.ui.sharedviewmodel"
            value.startsWith(":domain:usecase-") -> "com.velord.usecase.${leafName.removePrefix("usecase-").replace('-', '.')}"
            value.startsWith(":data:") -> "com.velord.data.${leafName.replace('-', '.')}"
            value.startsWith(":core:core-") -> "com.velord.core.${leafName.removePrefix("core-").replace('-', '.')}"
            value.startsWith(":infrastructure:") -> "com.velord.infrastructure.${leafName.replace('-', '.')}"
            value.startsWith(":ui:feature-") -> "com.velord.ui.feature.${leafName.removePrefix("feature-").replace('-', '.')}"
            value.startsWith(":ui:widget-") -> "com.velord.ui.widget.${leafName.removePrefix("widget-").replace('-', '.')}"
            else -> null
        }

    override fun toString(): String = value

    private companion object {
        private const val sharedViewModelName = "sharedviewmodel"
        private val lowerCaseLeafRegex = Regex("""[a-z0-9]+(?:-[a-z0-9]+)*""")
        private val usecaseRegex = Regex("""usecase-[a-z0-9]+(?:-[a-z0-9]+)*""")
        private val coreRegex = Regex("""core-[a-z0-9]+(?:-[a-z0-9]+)*""")
        private val featureRegex = Regex("""feature-[a-z0-9]+(?:-[a-z0-9]+)*""")
        private val widgetRegex = Regex("""widget-[a-z0-9]+(?:-[a-z0-9]+)*""")
    }
}
