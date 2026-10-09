package com.velord.infrastructure.konsist

import com.velord.infrastructure.konsist.codestyle.locateRepoRoot
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val BUILD_FILE_NAME = "build.gradle.kts"
private val INCLUDE_CALL_REGEX = Regex("""^\s*include\s*\(""")
private val SKIPPED_DIRECTORY_NAMES = setOf(".git", ".gradle", ".idea", "build")

class ActiveProjectSourceTest {

    private val repoRoot = locateRepoRoot()

    @Test
    fun `every included module source and build file remains governed`() {
        val includeCallCount = File(repoRoot, "settings.gradle.kts")
            .readLines()
            .count { line -> INCLUDE_CALL_REGEX.containsMatchIn(line) }
        val moduleDirectoryRoster = ActiveProjectSource.moduleDirectoryRoster
        assertTrue(moduleDirectoryRoster.isNotEmpty())
        assertEquals(includeCallCount, moduleDirectoryRoster.size)

        val sourceFileRoster = moduleDirectoryRoster.flatMap { directory ->
            val moduleDirectory = File(repoRoot, directory)
            val buildFile = File(moduleDirectory, BUILD_FILE_NAME)
            assertTrue(buildFile.isFile, buildFile.path)
            assertTrue(ActiveProjectSource.containsBuildFile(buildFile.path), buildFile.path)
            assertTrue(ActiveProjectSource.containsGradleScript(buildFile.path), buildFile.path)

            val sourceDirectory = File(moduleDirectory, "src")
            sourceDirectory.walkTopDown()
                .filter { file -> file.isFile && file.extension == "kt" }
                .toList()
        }
        assertTrue(sourceFileRoster.isNotEmpty())
        val governedSourcePathRoster = ActiveProjectSource.fileRoster
            .map { file -> File(file.path).canonicalPath }
            .toSet()
        sourceFileRoster.forEach { file ->
            assertTrue(ActiveProjectSource.containsSource(file.path), file.path)
            assertTrue(file.canonicalPath in governedSourcePathRoster, file.path)
        }
    }

    @Test
    fun `root and standalone builds stay outside active module source`() {
        val rootBuildFile = File(repoRoot, BUILD_FILE_NAME)
        assertFalse(ActiveProjectSource.containsSource(rootBuildFile.path))
        assertFalse(ActiveProjectSource.containsBuildFile(rootBuildFile.path))
        assertTrue(ActiveProjectSource.containsGradleScript(rootBuildFile.path))

        val standaloneBuildRoster = repoRoot.walkTopDown()
            .onEnter { directory -> directory.name !in SKIPPED_DIRECTORY_NAMES }
            .filter { file -> file.isFile && file.name == BUILD_FILE_NAME }
            .filter { file -> file.parentFile != repoRoot }
            .filterNot { file -> ActiveProjectSource.containsBuildFile(file.path) }
            .toList()

        standaloneBuildRoster.forEach { buildFile ->
            assertFalse(ActiveProjectSource.containsGradleScript(buildFile.path), buildFile.path)
            val sourceDirectory = File(buildFile.parentFile, "src")
            sourceDirectory.walkTopDown()
                .filter { file -> file.isFile && file.extension == "kt" }
                .forEach { file ->
                    assertFalse(ActiveProjectSource.containsSource(file.path), file.path)
                }
        }
    }
}
