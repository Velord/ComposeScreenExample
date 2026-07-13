package com.velord.infrastructure.konsist.architecture.module

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFunctionDeclaration
import com.lemonappdev.konsist.api.declaration.KoPropertyDeclaration
import com.lemonappdev.konsist.api.declaration.KoTypeAliasDeclaration
import com.lemonappdev.konsist.api.declaration.combined.KoClassAndInterfaceAndObjectDeclaration
import com.lemonappdev.konsist.api.ext.koscope.declarationsOf
import com.lemonappdev.konsist.api.provider.KoModuleProvider
import com.lemonappdev.konsist.api.provider.KoReceiverTypeProvider
import com.lemonappdev.konsist.api.provider.KoSourceSetProvider
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val UI_FOLDER_NAME = "ui"
private const val BUILD_FILE_NAME = "build.gradle.kts"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private val DATA_PROJECT_ACCESSOR_REGEX = Regex("""projects\.data\.[A-Za-z][A-Za-z0-9]*""")
private val DATA_PROJECT_PATH_REGEX = Regex("""project\(\s*[\"']:data:[^\"']+[\"']\s*\)""")
private val SAME_TARGET_EXTENSION_DEBT_ROSTER = setOf(
    "AndroidBottomNavigationGraphItem:toMultipleBackstackGraphItem",
    "VoyagerScreen:toTab",
)

class ModuleDependencyTest {

    private val repoRoot = locateRepoRoot()
    private val projectScope = Konsist.scopeFromProject()

    @Test
    fun `ui modules should not depend directly on data modules`() {
        val violationRoster = File(repoRoot, UI_FOLDER_NAME)
            .walkTopDown()
            .filter { file -> file.name == BUILD_FILE_NAME }
            .flatMap { file ->
                file.readLines().withIndex().mapNotNull { (lineIndex, line) ->
                    line.takeIf(::isDirectDataDependency)?.let {
                        "${file.relativeTo(repoRoot).path}:${lineIndex + 1}"
                    }
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "UI modules depend directly on data modules: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `project owned types should not have extensions in their owning target`() {
        val functionViolationRoster = projectScope
            .declarationsOf<KoFunctionDeclaration>()
            .filter { function -> function.hasSameModuleReceiver() }
            .map { function -> "${function.containingFile.name}:${function.name}" }
        val propertyViolationRoster = projectScope
            .declarationsOf<KoPropertyDeclaration>()
            .filter { property -> property.hasSameModuleReceiver() }
            .map { property -> "${property.containingFile.name}:${property.name}" }
        val violationRoster = (functionViolationRoster + propertyViolationRoster).toSet()
        val unexpectedViolationRoster = violationRoster - SAME_TARGET_EXTENSION_DEBT_ROSTER
        val staleDebtRoster = SAME_TARGET_EXTENSION_DEBT_ROSTER - violationRoster

        if (unexpectedViolationRoster.isNotEmpty()) {
            val msg = "Unexpected same-target extensions: " +
                unexpectedViolationRoster.joinToString()
            println(msg)
        }
        if (staleDebtRoster.isNotEmpty()) {
            val msg = "Remove migrated same-target extension debt: " + staleDebtRoster.joinToString()
            println(msg)
        }

        assertTrue(unexpectedViolationRoster.isEmpty())
        assertTrue(staleDebtRoster.isEmpty())
    }

    private fun isDirectDataDependency(line: String): Boolean {
        if (line.trimStart().startsWith("//")) return false

        return DATA_PROJECT_ACCESSOR_REGEX.containsMatchIn(line) ||
            DATA_PROJECT_PATH_REGEX.containsMatchIn(line)
    }

    private fun KoReceiverTypeProvider.hasSameModuleReceiver(): Boolean {
        val declarationModule = this as KoModuleProvider
        val receiverDeclaration = receiverType?.sourceDeclaration ?: return false
        val isProjectOwnedType = receiverDeclaration is KoClassAndInterfaceAndObjectDeclaration ||
            receiverDeclaration is KoTypeAliasDeclaration
        if (isProjectOwnedType.not()) return false

        val receiverModule = receiverDeclaration as KoModuleProvider
        if (declarationModule.moduleName != receiverModule.moduleName) return false

        val declarationSourceSet = (this as KoSourceSetProvider).sourceSetName
        val receiverSourceSet = (receiverDeclaration as KoSourceSetProvider).sourceSetName
        return declarationSourceSet == receiverSourceSet
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
}
