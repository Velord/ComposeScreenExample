package com.velord.infrastructure.konsist.architecture.module

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val BUILD_LOGIC_MODULE_PATH = "build-logic"
private const val APP_MODULE_PATH = ":app"
private const val MODEL_MODULE_PATH = ":model"
private const val SHARED_VIEW_MODEL_MODULE_PATH = ":ui:sharedviewmodel"
private const val BUILD_LOGIC_PACKAGE_ROOT = "com.velord.buildlogic"
private const val APP_PACKAGE_ROOT = "com.velord.composescreenexample"
private const val MODEL_PACKAGE_ROOT = "com.velord.model"
private const val SHARED_VIEW_MODEL_PACKAGE_ROOT = "com.velord.ui.sharedviewmodel"
private const val SHARED_VIEW_MODEL_NAME = "sharedviewmodel"
private const val SOURCE_FOLDER_NAME = "src"
private const val KOTLIN_FOLDER_NAME = "kotlin"
private const val JAVA_FOLDER_NAME = "java"
private val LOWER_CASE_LEAF_REGEX = Regex("""[a-z0-9]+(?:-[a-z0-9]+)*""")
private val USE_CASE_REGEX = Regex("""usecase-[a-z0-9]+(?:-[a-z0-9]+)*""")
private val CORE_REGEX = Regex("""core-[a-z0-9]+(?:-[a-z0-9]+)*""")
private val FEATURE_REGEX = Regex("""feature-[a-z0-9]+(?:-[a-z0-9]+)*""")
private val WIDGET_REGEX = Regex("""widget-[a-z0-9]+(?:-[a-z0-9]+)*""")
private val PACKAGE_DIRECTIVE_REGEX = Regex("""(?m)^\uFEFF?package\s+([A-Za-z0-9_.]+)\s*$""")

class ModuleNamingTest {

    private val repoRoot = locateRepoRoot()
    private val projectFileRoster = Konsist.scopeFromProject().files

    @Test
    fun `kotlin file should live under an allowed module naming family`() {
        projectFileRoster
            .filter { file ->
                val modulePath = modulePathOrNull(file.path)
                modulePath != null && file.path.contains("${File.separator}src${File.separator}")
            }
            .assertTrue { file ->
                val modulePath = modulePathOrNull(file.path)
                val isValid = modulePath != null && modulePath.matchesNamingFamily()

                if (isValid.not()) {
                    println(
                        "Name: ${file.name}. FAILED. " +
                                "Module path '$modulePath' does not match " +
                                "the allowed naming families."
                    )
                }

                isValid
            }
    }

    @Test
    fun `kotlin file package root should match its module path`() {
        projectFileRoster
            .filter { file ->
                val modulePath = modulePathOrNull(file.path)
                val packageName = file.packagee?.name
                modulePath != null &&
                        packageName != null &&
                        file.path.contains("${File.separator}src${File.separator}")
            }
            .assertTrue { file ->
                val modulePath = modulePathOrNull(file.path) ?: return@assertTrue false
                val packageName = file.packagee?.name ?: return@assertTrue false
                val expectedPackageRoot = modulePath.expectedPackageRoot()
                val isValid = expectedPackageRoot?.let { expectedPackage ->
                    packageName == expectedPackage || packageName.startsWith("$expectedPackage.")
                } == true

                if (isValid.not()) {
                    println(
                        "Name: ${file.name}. FAILED. " +
                                "Package '$packageName' does not match module '$modulePath'. " +
                                "Expected root: '$expectedPackageRoot'."
                    )
                }

                isValid
            }
    }

    @Test
    fun `package directive should match kotlin file location`() {
        projectFileRoster
            .filter { file -> expectedPackageNameOrNull(file.path) != null }
            .assertTrue { file ->
                val packageName = declaredPackageNameOrNull(file.text)
                val expectedPackageName = expectedPackageNameOrNull(file.path)
                val isValid = packageName == expectedPackageName

                if (isValid.not()) {
                    val msg = "Name: ${file.name}. FAILED. " +
                        "Package '$packageName' does not match file location. " +
                        "Expected: '$expectedPackageName'."
                    println(msg)
                }

                isValid
            }
    }

    @Test
    fun `module naming governance should centralize core module path and package root literals`() {
        val file = projectFileRoster.firstOrNull { file ->
            file.path.endsWith("ModuleNamingTest.kt")
        } ?: error("ModuleNamingTest.kt not found in project scope")

        val literalRoster = listOf(
            BUILD_LOGIC_MODULE_PATH,
            APP_MODULE_PATH,
            MODEL_MODULE_PATH,
            SHARED_VIEW_MODEL_MODULE_PATH,
            BUILD_LOGIC_PACKAGE_ROOT,
            APP_PACKAGE_ROOT,
            MODEL_PACKAGE_ROOT,
            SHARED_VIEW_MODEL_PACKAGE_ROOT,
        )

        val violationRoster = literalRoster.filter { literal ->
            file.text.split("\"$literal\"").size - 1 > 1
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Name: ${file.name}. FAILED. " +
                    "Duplicate governance literals must be centralized: " +
                    "${violationRoster.joinToString()}."
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, "settings.gradle.kts").isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")

    private fun modulePathOrNull(filePath: String): ModulePath? {
        val relativePath = File(filePath).absoluteFile.relativeTo(repoRoot).invariantSeparatorsPath

        val pathSegmentRoster = relativePath.split('/')
        if (pathSegmentRoster.isEmpty()) return null

        if (pathSegmentRoster.first() == BUILD_LOGIC_MODULE_PATH) {
            return ModulePath(BUILD_LOGIC_MODULE_PATH)
        }

        val srcIndex = pathSegmentRoster.indexOf("src")
        if (srcIndex <= 0) return null

        val moduleSegmentRoster = pathSegmentRoster.take(srcIndex)
        return ModulePath(":" + moduleSegmentRoster.joinToString(":"))
    }

    private fun expectedPackageNameOrNull(filePath: String): String? {
        val pathSegmentRoster = File(filePath).invariantSeparatorsPath.split('/')
        val sourceIndex = pathSegmentRoster.indexOf(SOURCE_FOLDER_NAME)
        if (sourceIndex == -1) return null

        val sourceRootIndex = pathSegmentRoster.indexOfFirst { segment ->
            segment == KOTLIN_FOLDER_NAME || segment == JAVA_FOLDER_NAME
        }
        if (sourceRootIndex <= sourceIndex) return null
        if (sourceRootIndex >= pathSegmentRoster.lastIndex) return null

        return pathSegmentRoster
            .subList(sourceRootIndex + 1, pathSegmentRoster.lastIndex)
            .joinToString(".")
    }

    private fun declaredPackageNameOrNull(fileText: String): String? =
        PACKAGE_DIRECTIVE_REGEX.find(fileText)?.groupValues?.get(1)
}

private data class ModulePath(val value: String) {

    private val segmentRoster = value.removePrefix(":").split(":")
    private val leafName = segmentRoster.last()

    fun matchesNamingFamily(): Boolean = when {
        value == BUILD_LOGIC_MODULE_PATH -> true
        value == APP_MODULE_PATH -> true
        value == MODEL_MODULE_PATH -> true
        value == SHARED_VIEW_MODEL_MODULE_PATH -> true
        value.startsWith(":domain:") ->
            segmentRoster.size == 2 && leafName.matches(USE_CASE_REGEX)
        value.startsWith(":data:") ->
            segmentRoster.size == 2 && leafName.matches(LOWER_CASE_LEAF_REGEX)
        value.startsWith(":core:") ->
            segmentRoster.size == 2 && leafName.matches(CORE_REGEX)
        value.startsWith(":infrastructure:") ->
            segmentRoster.size == 2 && leafName.matches(LOWER_CASE_LEAF_REGEX)
        value.startsWith(":ui:") -> segmentRoster.size == 2 && (
                leafName.matches(FEATURE_REGEX) ||
                        leafName == SHARED_VIEW_MODEL_NAME ||
                        leafName.matches(WIDGET_REGEX)
                )
        else -> false
    }

    fun expectedPackageRoot(): String? = when {
        value == BUILD_LOGIC_MODULE_PATH -> BUILD_LOGIC_PACKAGE_ROOT
        value == APP_MODULE_PATH -> APP_PACKAGE_ROOT
        value == MODEL_MODULE_PATH -> MODEL_PACKAGE_ROOT
        value == SHARED_VIEW_MODEL_MODULE_PATH -> SHARED_VIEW_MODEL_PACKAGE_ROOT
        value.startsWith(":domain:usecase-") ->
            "com.velord.usecase.${leafName.removePrefix("usecase-").replace('-', '.')}"
        value.startsWith(":data:") -> "com.velord.data.${leafName.replace('-', '.')}"
        value.startsWith(":core:core-") ->
            "com.velord.core.${leafName.removePrefix("core-").replace('-', '.')}"
        value.startsWith(":infrastructure:") ->
            "com.velord.infrastructure.${leafName.replace('-', '.')}"
        value.startsWith(":ui:feature-") ->
            "com.velord.ui.feature.${leafName.removePrefix("feature-").replace('-', '.')}"
        value.startsWith(":ui:widget-") ->
            "com.velord.ui.widget.${leafName.removePrefix("widget-").replace('-', '.')}"
        else -> null
    }

    override fun toString(): String = value
}
