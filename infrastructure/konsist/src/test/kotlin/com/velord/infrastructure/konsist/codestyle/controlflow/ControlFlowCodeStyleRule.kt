package com.velord.infrastructure.konsist.codestyle.controlflow

import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.joinLine
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtParenthesizedExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.kotlin.psi.KtWhenEntry
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType

internal fun KtFile.whenBranchLineRoster(): Set<Int> = collectDescendantsOfType<KtWhenEntry>()
    .mapNotNull { entry -> entry.arrow?.textOffset }
    .map { offset -> text.take(offset).count { it == '\n' } }
    .toSet()

internal fun KtFile.unbracedIfElseLineRoster(): List<Int> = collectDescendantsOfType<KtIfExpression>()
    .filter { expression ->
        if (expression.isAssignedToVariable().not()) return@filter false
        val elseBranch = expression.`else` ?: return@filter false
        val isMultiline = '\n' in expression.text
        val hasUnbracedThen = expression.then !is KtBlockExpression
        val hasUnbracedElse = elseBranch !is KtBlockExpression && elseBranch !is KtIfExpression
        isMultiline && (hasUnbracedThen || hasUnbracedElse)
    }
    .map { expression -> text.take(expression.textOffset).count { it == '\n' } + 1 }

private fun KtIfExpression.isAssignedToVariable(): Boolean {
    var valueExpression: KtExpression = this
    while (valueExpression.parent is KtParenthesizedExpression) {
        valueExpression = valueExpression.parent as KtParenthesizedExpression
    }

    return when (val owner = valueExpression.parent) {
        is KtProperty -> owner.initializer == valueExpression
        is KtPropertyAccessor -> owner.bodyExpression == valueExpression
        is KtBinaryExpression -> owner.operationToken == KtTokens.EQ &&
                owner.right == valueExpression
        else -> false
    }
}

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
