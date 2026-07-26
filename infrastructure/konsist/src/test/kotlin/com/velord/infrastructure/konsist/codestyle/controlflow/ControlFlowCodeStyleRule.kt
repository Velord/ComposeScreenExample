package com.velord.infrastructure.konsist.codestyle.controlflow

import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.joinLine

internal fun isSplitIfOpeningCondition(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed.endsWith("if (").not()) return false
    if (nextLineTrimmed.isBlank()) return false

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun isMissingBlankLineAfterGuardReturns(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trim()
    val nextLineTrimmed = nextLine.trimStart()
    val isGuardReturnLine = currentLineTrimmed == "}" ||
            currentLineTrimmed.contains(" return ") ||
            currentLineTrimmed.startsWith("if (") &&
            currentLineTrimmed.endsWith("return false") ||
            currentLineTrimmed.startsWith("if (") &&
            currentLineTrimmed.endsWith("return null")
    if (isGuardReturnLine.not()) return false
    if (nextLineTrimmed.startsWith("return ").not()) return false

    return nextLine.isBlank().not()
}

internal fun isBlankLineBetweenWhenBranches(
    previousLine: String,
    currentLine: String,
    nextLine: String,
): Boolean {
    if (currentLine.isBlank().not()) return false
    if (previousLine.trimStart().contains("->").not()) return false

    val nextLineTrimmed = nextLine.trimStart()
    return nextLineTrimmed.startsWith("is ") ||
            nextLineTrimmed.startsWith("else ->")
}

internal fun isSingleExpressionWhenBranchWithBraces(
    currentLine: String,
    nextLine: String,
    thirdLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimStart()
    val nextLineTrimmed = nextLine.trimStart()
    val thirdLineTrimmed = thirdLine.trimStart()
    if (currentLineTrimmed.contains("-> {").not()) return false
    if (nextLineTrimmed.isBlank()) return false
    if (nextLineTrimmed.endsWith("{")) return false
    if (nextLineTrimmed.contains("=")) return false

    return thirdLineTrimmed == "}"
}
