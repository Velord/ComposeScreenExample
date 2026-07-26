package com.velord.infrastructure.konsist.codestyle.expression

import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.joinLine

private val EXPRESSION_BODY_OPERATOR_MULTILINE_CALL_REGEX = Regex(
    """=\s+.+\s[+\-*/]\s+[A-Za-z_][A-Za-z0-9_]*\("""
)

internal fun isShortWrappedDeclaration(
    currentLine: String,
    nextLine: String,
    thirdLine: String?,
): Boolean {
    if (currentLine.trimEnd().endsWith("=").not()) return false

    val nextLineTrimmed = nextLine.trimStart()
    if (nextLineTrimmed.isBlank()) return false
    if (nextLineTrimmed.startsWith("//")) return false
    if (nextLineTrimmed.startsWith("/*")) return false
    if (nextLineTrimmed.startsWith("*")) return false
    if (nextLineTrimmed.endsWith("+")) return false
    if (nextLineTrimmed.endsWith("-")) return false
    if (nextLineTrimmed.endsWith("&&")) return false
    if (nextLineTrimmed.endsWith("||")) return false
    if (nextLineTrimmed.endsWith("?:")) return false
    if (nextLineTrimmed.endsWith("(")) return false

    val thirdLineTrimmed = thirdLine?.trimStart().orEmpty()
    if (thirdLineTrimmed.startsWith(".")) return false
    if (thirdLineTrimmed.startsWith("?.")) return false
    if (thirdLineTrimmed.startsWith("?:")) return false
    if (thirdLineTrimmed.startsWith("+")) return false
    if (thirdLineTrimmed.startsWith("&&")) return false
    if (thirdLineTrimmed.startsWith("||")) return false

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun isSplitShortElvisExpression(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val currentLineStartTrimmed = currentLine.trimStart()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineStartTrimmed.startsWith(".")) return false
    if (currentLineStartTrimmed.startsWith("?.")) return false
    if (currentLineTrimmed.contains("=").not()) return false
    if (nextLineTrimmed.startsWith("?:").not()) return false

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun isSingleReturnBlockFunction(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed.contains("fun ").not()) return false
    if (currentLineTrimmed.endsWith("{").not()) return false
    if (nextLineTrimmed.startsWith("return ").not()) return false

    return joinLine(
        currentLine = currentLineTrimmed.removeSuffix("{").trimEnd(),
        nextLine = nextLineTrimmed.removePrefix("return ").trimStart(),
    ).length <= HARD_WRAP
}

internal fun isSplitExpressionBodyOpeningCall(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed.endsWith("=").not()) return false
    if (nextLineTrimmed.startsWith("when ") ||
        nextLineTrimmed.startsWith("when{")
    ) {
        return false
    }
    if (nextLineTrimmed.contains("(").not()) return false
    if (nextLineTrimmed.startsWith(".") ||
        nextLineTrimmed.startsWith("?.") ||
        nextLineTrimmed.startsWith("?:")
    ) {
        return false
    }

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun isInlineParameterFunctionWithWrappedOpeningCall(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (currentLineTrimmed.contains("fun ").not()) return false
    if (currentLineTrimmed.endsWith("=").not()) return false
    if (nextLineTrimmed.contains("(").not()) return false
    if (nextLineTrimmed.startsWith(".") || nextLineTrimmed.startsWith("?.")) return false
    if (nextLineTrimmed.endsWith("(").not() &&
        nextLineTrimmed.endsWith("{").not() &&
        nextLineTrimmed.endsWith("[").not()
    ) {
        return false
    }

    val parameterStart = currentLineTrimmed.indexOf('(')
    val parameterEnd = currentLineTrimmed.lastIndexOf(')')
    if (parameterStart < 0 || parameterEnd <= parameterStart + 1) return false

    val declarationClose = currentLineTrimmed.substring(parameterEnd).removeSuffix("=").trimEnd()
    val joinedClosingLine = currentLine.takeWhile(Char::isWhitespace) +
        declarationClose + " = " + nextLineTrimmed

    return joinedClosingLine.length <= HARD_WRAP
}

internal fun isSplitBlockBodyPropertyGetterOpening(
    currentLine: String,
    nextLine: String,
): Boolean {
    val currentLineStartTrimmed = currentLine.trimStart()
    val currentLineTrimmed = currentLine.trimEnd()
    val nextLineTrimmed = nextLine.trimStart()
    if (nextLineTrimmed.startsWith("get() {").not()) return false
    if (currentLineTrimmed.endsWith("{") || currentLineTrimmed.endsWith("=")) return false
    if (currentLineStartTrimmed.startsWith("val ").not() &&
        currentLineStartTrimmed.startsWith("var ").not() &&
        currentLineStartTrimmed.contains(" val ").not() &&
        currentLineStartTrimmed.contains(" var ").not()
    ) {
        return false
    }

    return joinLine(currentLine, nextLine).length <= HARD_WRAP
}

internal fun isExpressionBodyOperatorBeforeMultilineCall(line: String): Boolean {
    val lineTrimmed = line.trimEnd()
    if (lineTrimmed.endsWith("(").not()) return false
    if (lineTrimmed.contains("=").not()) return false

    return EXPRESSION_BODY_OPERATOR_MULTILINE_CALL_REGEX.containsMatchIn(lineTrimmed)
}