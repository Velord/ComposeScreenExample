package com.velord.infrastructure.konsist.codestyle.call

import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.joinLine

private const val LOOKBACK_LINE_COUNT = 6
private val COLLECTION_CALL_REGEX = Regex(
    "^\\.(" +
        "firstOrNull|first|lastOrNull|last|singleOrNull|single|" +
        "find|any|all|none|count|map|filter|filterNot" +
        ")\\b",
)
private val EXPLICIT_LAMBDA_PARAMETER_REGEX = Regex("""\{\s*[A-Za-z_][A-Za-z0-9_]*\s*->""")
// TOOD: rid of it as it can be changed
private val BUILT_IN_COMPOSE_CALL_NAME_ROSTER = setOf(
    "AsyncImage",
    "Button",
    "CameraPreviewView",
    "ExtendedFloatingActionButton",
    "FloatingActionButton",
    "Icon",
    "IconButton",
    "Image",
    "NavigationBarItem",
    "RadioButton",
    "Snackbar",
    "Text",
    "TextButton",
)
private val EXPRESSION_BODY_MAPPING_CALL_REGEX = Regex(
    """\bfun\b.+?\(([^)]*)\)\s*:\s*([A-Z][A-Za-z0-9_]*)\s*=\s*\2\(""",
)

internal fun isSplitOneOrTwoArgumentCall(
    composeCallNameRoster: Set<String>,
    currentLine: String,
    nextLine: String?,
    thirdLine: String?,
    fourthLine: String?,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine?.trim().orEmpty()
    val thirdLineTrimmed = thirdLine?.trim().orEmpty()
    val fourthLineTrimmed = fourthLine?.trim().orEmpty()
    if (currentLineTrimmed.endsWith("(").not()) return false
    if (isDeclarationOpening(currentLineTrimmed)) return false
    if (isComposeCallOpening(composeCallNameRoster, currentLineTrimmed)) return false
    if (isExpressionBodyMappingCallOpening(currentLineTrimmed)) return false
    if (nextLineTrimmed.endsWith(",").not()) return false
    if (thirdLineTrimmed == ")") {
        val joinedLine = currentLineTrimmed.removeSuffix("(") +
            "(${nextLineTrimmed.removeSuffix(",")})"
        return joinedLine.length <= HARD_WRAP
    }
    if (thirdLineTrimmed.endsWith(",").not()) return false
    if (fourthLineTrimmed != ")") return false

    val joinedLine = currentLineTrimmed.removeSuffix("(") +
        "(${nextLineTrimmed.removeSuffix(",")}, ${thirdLineTrimmed.removeSuffix(",")})"
    return joinedLine.length <= HARD_WRAP
}

internal fun isDeclarationOpening(line: String): Boolean {
    val lineTrimmed = line.trimStart()
    if (lineTrimmed.startsWith("@")) return true
    if (lineTrimmed.contains(" fun ")) return true
    if (lineTrimmed.startsWith("fun ")) return true
    if (lineTrimmed.contains(" class ")) return true
    if (lineTrimmed.startsWith("class ")) return true
    if (lineTrimmed.startsWith("data class ")) return true
    if (lineTrimmed.startsWith("sealed class ")) return true
    if (lineTrimmed.startsWith("abstract class ")) return true
    if (lineTrimmed.startsWith("interface ")) return true

    return lineTrimmed.startsWith("internal interface ")
}

internal fun isCompactComposeCallWithSeveralParameters(
    composeCallNameRoster: Set<String>,
    line: String,
): Boolean {
    val lineTrimmed = line.trimStart()
    if (lineTrimmed.firstOrNull()?.isUpperCase() != true) return false
    if (lineTrimmed.contains("(").not()) return false
    if (lineTrimmed.contains(")").not()) return false
    if (lineTrimmed.endsWith("{")) return false

    val callName = lineTrimmed.substringBefore("(")
    if (callName.contains(".")) return false
    if (composeCallNameRoster.contains(callName).not()) return false

    return hasSeveralTopLevelArguments(lineTrimmed)
}

internal fun composeCallNameRoster(fileText: String): Set<String> {
    if (fileText.contains("@Composable").not()) return emptySet()

    val importedAliasNameRoster = fileText.lines().mapNotNull { line ->
        val importedName = line.substringBefore(" as ").substringAfterLast(".")
        if (line.startsWith("import ") &&
            line.contains(" as ") &&
            importedName in BUILT_IN_COMPOSE_CALL_NAME_ROSTER
        ) {
            line.substringAfter(" as ").trim()
        } else {
            null
        }
    }.toSet()
    val localNameRoster = fileText.lines()
        .zipWithNext()
        .filter { (previousLine, currentLine) ->
            previousLine.trim() == "@Composable" && currentLine.contains("fun ")
        }
        .map { (_, currentLine) ->
            currentLine.substringAfter("fun ")
                .substringBefore("(")
                .substringAfterLast(".")
        }
        .toSet()
    return BUILT_IN_COMPOSE_CALL_NAME_ROSTER + importedAliasNameRoster + localNameRoster
}

internal fun hasSeveralTopLevelArguments(line: String): Boolean {
    val argumentText = line
        .substringAfter("(")
        .substringBeforeLast(")")
    var depth = 0
    argumentText.forEach { char ->
        if (char == '(' || char == '[' || char == '{') depth++
        if (char == ')' || char == ']' || char == '}') depth--
        if (char == ',' && depth == 0) return true
    }

    return false
}

internal fun isComposeCallOpening(
    composeCallNameRoster: Set<String>,
    line: String,
): Boolean {
    val lineTrimmed = line.trimStart()
    if (lineTrimmed.firstOrNull()?.isUpperCase() != true) return false
    if (lineTrimmed.contains("(").not()) return false

    val callName = lineTrimmed
        .substringBefore("(")
        .substringAfter("=")
        .substringAfter("return ")
        .trim()
        .substringAfterLast(".")
    return composeCallNameRoster.contains(callName)
}

internal fun isExpressionBodyMappingCallOpening(line: String): Boolean {
    val match = EXPRESSION_BODY_MAPPING_CALL_REGEX.find(line) ?: return false
    val parameterText = match.groupValues[1]
    return topLevelArgumentCount(parameterText) == 1
}

internal fun isInlineExpressionBodyObjectMapping(line: String): Boolean {
    val match = EXPRESSION_BODY_MAPPING_CALL_REGEX.find(line) ?: return false
    val parameterText = match.groupValues[1]
    if (topLevelArgumentCount(parameterText) != 1) return false
    if (line.substring(match.range.last + 1).contains(")").not()) return false

    val argumentPart = line
        .substring(match.range.last + 1)
        .substringBeforeLast(")")
    return topLevelArgumentCount(argumentPart) > 1
}

internal fun topLevelArgumentCount(argumentText: String): Int {
    if (argumentText.isBlank()) return 0

    var depth = 0
    var count = 1
    argumentText.forEach { char ->
        if (char == '(' || char == '[' || char == '{') depth++
        if (char == ')' || char == ']' || char == '}') depth--
        if (char == ',' && depth == 0) count++
    }

    return count
}

internal fun isSplitSingleCollectionCallAfterShortReceiver(
    currentLine: String,
    nextLine: String,
    thirdLine: String?,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    val thirdLineTrimmed = thirdLine?.trimStart().orEmpty()
    if (currentLineTrimmed.contains("=").not()) return false
    if (COLLECTION_CALL_REGEX.containsMatchIn(nextLineTrimmed).not()) return false
    if (hasExplicitLambdaParameter(nextLineTrimmed) &&
        thirdLineTrimmed.isNotBlank()
    ) {
        return false
    }
    if (thirdLineTrimmed.startsWith(".")) return false
    if (thirdLineTrimmed.startsWith("?:")) return false

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun hasExplicitLambdaParameter(line: String): Boolean =
    EXPLICIT_LAMBDA_PARAMETER_REGEX.containsMatchIn(line)

internal fun isCallChainViolation(
    lineRoster: List<String>,
    lineIndex: Int,
): Boolean {
    val currentLine = lineRoster[lineIndex]
    val nextLine = lineRoster[lineIndex + 1]
    if (isWrappedChainLineWithMultipleCalls(currentLine)) return true

    return isInlineCallChainStartBeforeMultilineContinuation(
        currentLine = currentLine,
        nextLine = nextLine,
        thirdLine = lineRoster.getOrNull(lineIndex + 2),
    ) || isShortExpressionBodyChainSplitAfterInlineCall(
        lineRoster = lineRoster,
        lineIndex = lineIndex,
        currentLine = currentLine,
        nextLine = nextLine,
    ) || isClosedNonChainCallFollowedByShortDotCall(
        lineRoster = lineRoster,
        lineIndex = lineIndex,
        currentLine = currentLine,
        nextLine = nextLine,
    )
}

internal fun isClosedNonChainCallFollowedByShortDotCall(
    lineRoster: List<String>,
    lineIndex: Int,
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trim()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed != ")") return false
    if (nextLineTrimmed.startsWith(".").not()) return false

    val openingCallLine = findOpeningCallLineAbove(
        lineRoster = lineRoster,
        lineIndex = lineIndex,
    ) ?: return false
    val openingCallLineTrimmed = openingCallLine.trimStart()
    if (openingCallLineTrimmed.startsWith(".")) return false
    if (openingCallLineTrimmed.contains("= ").not() &&
        openingCallLineTrimmed.startsWith("return ").not()
    ) {
        return false
    }

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun findOpeningCallLineAbove(
    lineRoster: List<String>,
    lineIndex: Int,
): String? {
    var depth = 0
    for (candidateIndex in lineIndex downTo 0) {
        val line = lineRoster[candidateIndex]
        line.reversed().forEach { char ->
            if (char == ')') depth++
            if (char == '(') {
                if (depth == 0) return line
                depth--
                if (depth == 0) return line
            }
        }
    }

    return null
}

internal fun isShortExpressionBodyChainSplitAfterInlineCall(
    lineRoster: List<String>,
    lineIndex: Int,
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed.trimStart().startsWith(".")) return false
    if (currentLineTrimmed.endsWith(")").not() &&
        currentLineTrimmed.endsWith("}").not()
    ) {
        return false
    }
    if (nextLineTrimmed.startsWith(".").not()) return false
    if (hasExpressionBodyStartAbove(lineRoster, lineIndex).not()) return false

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun hasExpressionBodyStartAbove(
    lineRoster: List<String>,
    lineIndex: Int,
): Boolean {
    val startIndex = maxOf(0, lineIndex - LOOKBACK_LINE_COUNT)
    return (startIndex..lineIndex).any { candidateIndex ->
        lineRoster[candidateIndex].trimEnd().endsWith("=")
    }
}

internal fun isInlineCallChainStartBeforeMultilineContinuation(
    currentLine: String,
    nextLine: String,
    thirdLine: String?,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val currentLineStartTrimmed = currentLine.trimStart()
    if (currentLineStartTrimmed.startsWith(".") ||
        currentLineStartTrimmed.startsWith("?.") ||
        currentLineStartTrimmed.startsWith("import ") ||
        currentLineStartTrimmed.startsWith(").") ||
        currentLineStartTrimmed.startsWith("}.") ||
        currentLineStartTrimmed.startsWith("].")
    ) {
        return false
    }
    if (currentLineTrimmed.contains("//")) return false

    val nextLineTrimmed = nextLine.trimStart()
    val thirdLineTrimmed = thirdLine?.trimStart().orEmpty()
    val hasMultilineContinuation = nextLineTrimmed.startsWith(".") ||
        ((nextLineTrimmed.startsWith("//") || nextLineTrimmed.isBlank()) &&
            thirdLineTrimmed.startsWith("."))
    if (hasMultilineContinuation.not()) return false

    val chainSegmentCount = Regex("""\.\w+\(""")
        .findAll(currentLineTrimmed)
        .count()
    val closesThenChains = currentLineTrimmed.contains(").") ||
        currentLineTrimmed.contains("}.") ||
        currentLineTrimmed.contains("].")

    return closesThenChains || chainSegmentCount >= 2
}

internal fun isWrappedChainLineWithMultipleCalls(line: String): Boolean {
    val lineTrimmed = line.trimStart()
    if (lineTrimmed.startsWith(".").not() &&
        lineTrimmed.startsWith(").").not() &&
        lineTrimmed.startsWith("}.").not() &&
        lineTrimmed.startsWith("].").not()
    ) {
        return false
    }
    if (lineTrimmed.startsWith(").") ||
        lineTrimmed.startsWith("}.") ||
        lineTrimmed.startsWith("].")
    ) {
        return Regex("""^[)}]]\.\w+(?:\(|\s*\{)""")
            .containsMatchIn(lineTrimmed)
    }

    var depth = 0
    var braceDepth = 0
    for (charIndex in 1 until lineTrimmed.lastIndex) {
        val char = lineTrimmed[charIndex]
        if (char == '(') depth++
        if (char == ')') depth--
        if (char == '{') braceDepth++
        if (char == '}') braceDepth--
        if (char == '.' && depth == 0 && braceDepth == 0) {
            val chainedCall = lineTrimmed.substring(charIndex)
            return Regex("""^\.\w+(?:\(|\s*\{)""")
                .containsMatchIn(chainedCall)
        }
    }

    return false
}
