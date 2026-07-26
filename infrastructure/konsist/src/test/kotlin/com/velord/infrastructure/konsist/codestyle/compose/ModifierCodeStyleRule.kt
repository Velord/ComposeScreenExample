package com.velord.infrastructure.konsist.codestyle.compose

private const val INTERNAL_VISIBILITY = "internal"
private val COMPOSABLE_FUNCTION_REGEX = Regex(
    """@Composable\s+(private|internal)\s+fun\s+""" +
        """(?:[A-Za-z_][A-Za-z0-9_]*\.)?([A-Za-z_][A-Za-z0-9_]*)\s*\(""",
)
private val DEFAULT_MODIFIER_REGEX = Regex("""modifier\s*:\s*Modifier\s*=\s*Modifier""")

internal data class ModifierSource(
    val name: String,
    val packageName: String,
    val text: String,
)

internal data class DefaultModifierViolation(
    val sourceName: String,
    val functionName: String,
)

private data class DefaultModifierCandidate(
    val visibility: String,
    val functionName: String,
    val parameterCount: Int,
    val openingParenthesisIndex: Int,
)

internal fun findUnnecessaryDefaultModifier(
    sourceRoster: List<ModifierSource>,
): DefaultModifierViolation? {
    sourceRoster.forEach { ownerSource ->
        defaultModifierCandidateRoster(ownerSource.text).forEach { candidate ->
            val hasCustomizedCaller = relevantSourceRoster(
                sourceRoster = sourceRoster,
                ownerSource = ownerSource,
                candidate = candidate,
            ).any { source ->
                hasCustomizedModifierCall(
                    source = source,
                    ownerSource = ownerSource,
                    candidate = candidate,
                )
            }
            if (hasCustomizedCaller.not()) {
                return DefaultModifierViolation(
                    sourceName = ownerSource.name,
                    functionName = candidate.functionName,
                )
            }
        }
    }

    return null
}

private fun defaultModifierCandidateRoster(text: String): List<DefaultModifierCandidate> =
    COMPOSABLE_FUNCTION_REGEX.findAll(text).mapNotNull { match ->
        val openingParenthesisIndex = match.range.last
        val closingParenthesisIndex = findClosingParenthesis(text, openingParenthesisIndex)
            ?: return@mapNotNull null
        val parameterText = text.substring(openingParenthesisIndex + 1, closingParenthesisIndex)
        val parameterRoster = topLevelSegmentRoster(parameterText)
        if (DEFAULT_MODIFIER_REGEX.matches(parameterRoster.lastOrNull()?.trim().orEmpty()).not()) {
            return@mapNotNull null
        }

        DefaultModifierCandidate(
            visibility = match.groupValues[1],
            functionName = match.groupValues[2],
            parameterCount = parameterRoster.size,
            openingParenthesisIndex = openingParenthesisIndex,
        )
    }.toList()

private fun relevantSourceRoster(
    sourceRoster: List<ModifierSource>,
    ownerSource: ModifierSource,
    candidate: DefaultModifierCandidate,
): List<ModifierSource> {
    if (candidate.visibility != INTERNAL_VISIBILITY) return listOf(ownerSource)

    val import = "import ${ownerSource.packageName}.${candidate.functionName}"
    return sourceRoster.filter { source ->
        source == ownerSource ||
            source.packageName == ownerSource.packageName ||
            source.text.lineSequence().any { line -> line == import }
    }
}

private fun hasCustomizedModifierCall(
    source: ModifierSource,
    ownerSource: ModifierSource,
    candidate: DefaultModifierCandidate,
): Boolean {
    val callRegex = Regex("""\b${Regex.escape(candidate.functionName)}\s*\(""")
    return callRegex.findAll(source.text).any { match ->
        val openingParenthesisIndex = match.range.last
        if (source == ownerSource &&
            openingParenthesisIndex == candidate.openingParenthesisIndex
        ) {
            return@any false
        }
        if (isFunctionDeclaration(source.text, openingParenthesisIndex)) return@any false

        val closingParenthesisIndex = findClosingParenthesis(source.text, openingParenthesisIndex)
            ?: return@any false
        val argumentText = source.text.substring(openingParenthesisIndex + 1, closingParenthesisIndex)
        val argumentCount = topLevelSegmentRoster(argumentText).size
        argumentText.contains("modifier =") || argumentCount >= candidate.parameterCount
    }
}

private fun isFunctionDeclaration(text: String, openingParenthesisIndex: Int): Boolean {
    val linePrefix = text.substring(0, openingParenthesisIndex).substringAfterLast('\n')
    return linePrefix.contains("fun ")
}

private fun findClosingParenthesis(text: String, openingParenthesisIndex: Int): Int? {
    var depth = 0
    text.substring(openingParenthesisIndex).forEachIndexed { offset, char ->
        if (char == '(') depth++
        if (char == ')') depth--
        if (depth == 0) return openingParenthesisIndex + offset
    }

    return null
}

private fun topLevelSegmentRoster(text: String): List<String> {
    if (text.isBlank()) return emptyList()

    val segmentRoster = mutableListOf<String>()
    var depth = 0
    var segmentStartIndex = 0
    text.forEachIndexed { index, char ->
        if (char == '(' || char == '[' || char == '{' || char == '<') depth++
        if (char == ')' || char == ']' || char == '}' || char == '>') depth--
        if (char == ',' && depth == 0) {
            segmentRoster += text.substring(segmentStartIndex, index)
            segmentStartIndex = index + 1
        }
    }
    val lastSegment = text.substring(segmentStartIndex)
    if (lastSegment.isNotBlank()) segmentRoster += lastSegment

    return segmentRoster
}
