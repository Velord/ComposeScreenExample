package com.velord.infrastructure.konsist.codestyle.governance

import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import java.io.File

private const val RECOMPOSE_HIGHLIGHTER_PATH =
    "core/core-ui/src/main/kotlin/com/velord/core/ui/util/modifier/RecomposeHighlighter.kt"

internal val governanceFileRoster = projectFileRoster.filter { file ->
    file.path.contains("infrastructure/konsist/src/test/kotlin/")
}
internal val hardWrapFileRoster = governanceFileRoster + projectFileRoster.filter { file ->
    file.path.replace(File.separatorChar, '/').endsWith(RECOMPOSE_HIGHLIGHTER_PATH)
}

internal fun isAllowedHardWrapTestName(
    line: String
): Boolean = line.trimStart().startsWith("fun `") && line.trimEnd().endsWith("`() {")

internal fun isLateConstDeclaration(
    lineRoster: List<String>,
    lineIndex: Int,
): Boolean {
    val currentLineTrimmed = lineRoster[lineIndex].trimStart()
    if (currentLineTrimmed.contains("const val ").not()) return false

    return lineRoster.take(lineIndex).any { line ->
        val previousLineTrimmed = line.trimStart()
        previousLineTrimmed.startsWith("class ") ||
            previousLineTrimmed.startsWith("internal class ") ||
            previousLineTrimmed.startsWith("private class ") ||
            previousLineTrimmed.startsWith("object ") ||
            previousLineTrimmed.startsWith("internal object ") ||
            previousLineTrimmed.startsWith("enum class ") ||
            previousLineTrimmed.startsWith("internal enum class ") ||
            previousLineTrimmed.startsWith("sealed class ") ||
            previousLineTrimmed.startsWith("internal sealed class ")
    }
}
