package com.velord.infrastructure.konsist.codestyle.declaration

import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.joinLine

private val TOP_LEVEL_MEMBER_REGEX = Regex(
    "^(?:(?:override|private|internal|actual|expect|abstract|suspend)\\s+)*(fun|val|var)\\b",
)
private val COMPANION_DEFAULT_VALUE_REGEX = Regex(
    "^(?:(?:private|internal|public|protected)\\s+)*" +
        "(?:const\\s+)?(?:val|var)\\s+[Dd]efault(?:\\s*[:=].*)?",
)
private val NESTED_TYPE_PREFIX_ROSTER = listOf(
    "class ",
    "data class ",
    "object ",
    "data object ",
    "sealed class ",
)

internal fun isBlankLineAfterFunctionOpeningBrace(
    currentLine: String,
    nextLine: String,
    thirdLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    if (currentLineTrimmed.contains("fun ").not()) return false
    if (currentLineTrimmed.endsWith("{").not()) return false
    if (nextLine.isBlank().not()) return false

    return thirdLine.isNotBlank() && thirdLine.trimStart() != "}"
}

internal fun isSplitSingleParameterClassHeader(
    currentLine: String,
    nextLine: String,
    thirdLine: String?,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trim()
    val thirdLineTrimmed = thirdLine?.trim().orEmpty()
    if (currentLineTrimmed.contains("class ").not()) return false
    if (currentLineTrimmed.endsWith("(").not()) return false
    if (nextLineTrimmed.startsWith("val ").not() &&
        nextLineTrimmed.startsWith("var ").not()) {
        return false
    }
    if (nextLineTrimmed.endsWith(",").not()) return false
    if (thirdLineTrimmed != ") {") return false

    return joinLine(
        currentLine = currentLineTrimmed.removeSuffix("("),
        nextLine = "$nextLineTrimmed ) {",
    ).length <= HARD_WRAP
}

internal fun isMissingBlankLineAfterCompactClassHeader(
    lineRoster: List<String>,
    lineIndex: Int,
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    if (isCompactClassHeader(currentLineTrimmed).not()) return false
    if (nextLine.trimStart().startsWith("@")) return false
    if (isSingleOneLineMemberCompactClass(lineRoster, lineIndex)) return false

    return nextLine.isBlank().not()
}

internal fun isBlankLineAfterCompactClassHeaderBeforeOneLineMember(
    lineRoster: List<String>,
    lineIndex: Int,
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    if (isCompactClassHeader(currentLineTrimmed).not()) return false
    if (nextLine.isBlank().not()) return false

    return isSingleOneLineMemberCompactClass(lineRoster, lineIndex)
}

internal fun isSingleOneLineMemberCompactClass(
    lineRoster: List<String>,
    lineIndex: Int,
): Boolean = countTopLevelMember(lineRoster, lineIndex) == 1 &&
    isOneLineMemberAllowedAfterCompactHeader(lineRoster[lineIndex + 1])

internal fun countTopLevelMember(
    lineRoster: List<String>,
    lineIndex: Int,
): Int {
    var depth = 1
    var count = 0
    for (candidateIndex in (lineIndex + 1)..lineRoster.lastIndex) {
        val line = lineRoster[candidateIndex]
        val lineTrimmed = line.trimStart()
        if (depth == 1 && TOP_LEVEL_MEMBER_REGEX.containsMatchIn(lineTrimmed)) count++

        depth += line.count { it == '{' }
        depth -= line.count { it == '}' }
        if (depth == 0) return count
    }

    return count
}

internal fun isCompactClassHeader(line: String): Boolean {
    if (line.startsWith("class ").not() &&
        line.startsWith("data class ").not() &&
        line.startsWith("value class ").not() &&
        line.startsWith("internal class ").not() &&
        line.startsWith("actual class ").not() &&
        line.startsWith("expect class ").not()
    ) {
        return false
    }

    return line.endsWith("{")
}

internal fun isOneLineMemberAllowedAfterCompactHeader(line: String): Boolean {
    val lineTrimmed = line.trimStart()
    if (lineTrimmed.startsWith("override fun ").not() &&
        lineTrimmed.startsWith("fun ").not() &&
        lineTrimmed.startsWith("override val ").not() &&
        lineTrimmed.startsWith("override var ").not()
    ) {
        return false
    }
    if (lineTrimmed.endsWith("{")) return false

    return lineTrimmed.contains("=") || lineTrimmed.startsWith("fun ")
}

internal fun isBlankLineAfterCompactSealedHeaderBeforeOneLineMember(
    currentLine: String,
    nextLine: String,
    thirdLine: String,
): Boolean {
    if (isCompactSealedHeader(currentLine).not()) return false
    if (nextLine.isBlank().not()) return false
    if (isNestedTypeDeclaration(thirdLine).not()) return false

    return thirdLine.trimEnd().endsWith("{").not()
}

internal fun isMissingBlankLineAfterCompactSealedHeaderBeforeNestedBody(
    currentLine: String,
    nextLine: String,
): Boolean {
    if (isCompactSealedHeader(currentLine).not()) return false
    if (isNestedTypeDeclaration(nextLine).not()) return false

    return nextLine.trimEnd().endsWith("{")
}

internal fun isBlankLineAfterCompanionObjectOpening(
    currentLine: String,
    nextLine: String,
): Boolean = currentLine.trimEnd() == "companion object {" && nextLine.isBlank()

internal fun isCompanionDefaultValueDeclaration(
    lineRoster: List<String>,
    lineIndex: Int,
    currentLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimStart()
    val isDefaultValue = COMPANION_DEFAULT_VALUE_REGEX.containsMatchIn(currentLineTrimmed)
    if (isDefaultValue.not()) return false

    return isInsideCompanionObject(lineRoster = lineRoster, lineIndex = lineIndex)
}

internal fun isInsideCompanionObject(
    lineRoster: List<String>,
    lineIndex: Int,
): Boolean {
    var depth = 0
    for (candidateIndex in lineIndex downTo 0) {
        val line = lineRoster[candidateIndex]
        depth -= line.count { it == '}' }
        depth += line.count { it == '{' }
        if (line.trimEnd() == "companion object {" && depth > 0) return true
    }

    return false
}
internal fun isBlankLineAfterCompactEnumHeader(
    currentLine: String,
    nextLine: String,
    thirdLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val thirdLineTrimmed = thirdLine.trimStart()
    if (currentLineTrimmed.startsWith("enum class ").not() &&
        currentLineTrimmed.startsWith("internal enum class ").not() &&
        currentLineTrimmed.startsWith("private enum class ").not()
    ) {
        return false
    }
    if (currentLineTrimmed.endsWith("{").not()) return false
    if (nextLine.isBlank().not()) return false
    if (thirdLineTrimmed.firstOrNull()?.isUpperCase() != true) return false

    return true
}

internal fun isBlankLineAfterCompactAbstractClassHeaderBeforeAbstractMember(
    currentLine: String,
    nextLine: String,
    thirdLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val thirdLineTrimmed = thirdLine.trimStart()
    if (currentLineTrimmed.startsWith("abstract class ").not() &&
        currentLineTrimmed.startsWith("internal abstract class ").not()
    ) {
        return false
    }
    if (currentLineTrimmed.endsWith("{").not()) return false
    if (nextLine.isBlank().not()) return false
    if (thirdLineTrimmed.startsWith("abstract fun ").not()) return false

    return true
}

private fun isCompactSealedHeader(line: String): Boolean {
    val lineTrimmed = line.trimEnd()
    if (lineTrimmed.startsWith("sealed class ").not() &&
        lineTrimmed.startsWith("internal sealed class ").not() &&
        lineTrimmed.startsWith("actual sealed class ").not()
    ) {
        return false
    }

    return lineTrimmed.endsWith("{")
}

private fun isNestedTypeDeclaration(line: String): Boolean {
    val lineTrimmed = line.trimStart()

    return NESTED_TYPE_PREFIX_ROSTER.any(lineTrimmed::startsWith)
}
