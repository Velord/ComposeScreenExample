package com.velord.infrastructure.konsist

import com.lemonappdev.konsist.api.Konsist
import java.io.File

private const val SETTINGS_FILE_NAME = "settings.gradle.kts"
private const val BUILD_FILE_NAME = "build.gradle.kts"
private const val BUILD_LOGIC_DIRECTORY = "build-logic"
private val INCLUDE_MODULE_REGEX = Regex("""(?m)^\s*include\("(:[^"]+)"\)\s*$""")

internal object ActiveProjectSource {

    private val repoRoot = generateSequence(File(System.getProperty("user.dir")).absoluteFile) {
        currentDirectory -> currentDirectory.parentFile
    }.first { currentDirectory ->
        File(currentDirectory, SETTINGS_FILE_NAME).isFile
    }
    val moduleDirectoryRoster = INCLUDE_MODULE_REGEX
        .findAll(File(repoRoot, SETTINGS_FILE_NAME).readText())
        .map { match -> match.groupValues[1].removePrefix(":").replace(':', '/') }
        .toSet()
    val projectScope by lazy { Konsist.scopeFromProject() }
    val fileRoster by lazy { projectScope.files.filter { file -> containsSource(file.path) } }

    fun containsSource(path: String): Boolean {
        val relativePath = relativePathOrNull(path) ?: return false

        return relativePath.startsWith("$BUILD_LOGIC_DIRECTORY/src/") ||
            moduleDirectoryRoster.any { directory ->
                relativePath.startsWith("$directory/src/")
            }
    }

    fun containsBuildFile(path: String): Boolean {
        val relativePath = relativePathOrNull(path) ?: return false

        return relativePath == "$BUILD_LOGIC_DIRECTORY/$BUILD_FILE_NAME" ||
            moduleDirectoryRoster.any { directory ->
                relativePath == "$directory/$BUILD_FILE_NAME"
            }
    }

    fun containsGradleScript(path: String): Boolean {
        val relativePath = relativePathOrNull(path) ?: return false

        return relativePath == BUILD_FILE_NAME ||
            relativePath.startsWith("$BUILD_LOGIC_DIRECTORY/") &&
            relativePath.endsWith(".gradle.kts") ||
            moduleDirectoryRoster.any { directory ->
                relativePath == "$directory/$BUILD_FILE_NAME"
            }
    }

    private fun relativePathOrNull(path: String): String? {
        val relativePath = runCatching {
            File(path).absoluteFile.relativeTo(repoRoot).invariantSeparatorsPath
        }

        return relativePath.getOrNull()
    }
}
