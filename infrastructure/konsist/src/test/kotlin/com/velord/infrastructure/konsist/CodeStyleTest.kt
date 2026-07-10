package com.velord.infrastructure.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val HARD_WRAP = 100
private const val LOOKBACK_LINE_COUNT = 6
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private const val RECOMPOSE_HIGHLIGHTER_PATH =
    "core/core-ui/src/main/kotlin/com/velord/core/ui/util/modifier/RecomposeHighlighter.kt"
private val WHITESPACE_REGEX = Regex("\\s+")
private val COLLECTION_CALL_REGEX = Regex(
    "^\\.(" +
        "firstOrNull|first|lastOrNull|last|singleOrNull|single|" +
        "find|any|all|none|count|map|filter|filterNot" +
        ")\\b",
)
private val EXPLICIT_LAMBDA_PARAMETER_REGEX = Regex("""\{\s*[A-Za-z_][A-Za-z0-9_]*\s*->""")
private val TOP_LEVEL_MEMBER_REGEX = Regex(
    "^(?:(?:override|private|internal|actual|expect|abstract|suspend)\\s+)*(fun|val|var)\\b",
)
// TOOD: rid of it as it can be changed
private val BUILT_IN_COMPOSE_CALL_NAME_ROSTER = setOf(
    "AsyncImage",
    "Button",
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
private val COMPANION_DEFAULT_VALUE_REGEX = Regex(
    "^(?:(?:private|internal|public|protected)\\s+)*" +
        "(?:const\\s+)?(?:val|var)\\s+[Dd]efault(?:\\s*[:=].*)?",
)
internal val projectFileRoster = Konsist.scopeFromProject().files
private val governanceFileRoster = projectFileRoster.filter { file ->
    file.path.contains("infrastructure/konsist/src/test/kotlin/")
}
private val hardWrapFileRoster = governanceFileRoster + projectFileRoster.filter { file ->
    file.path.replace(File.separatorChar, '/').endsWith(RECOMPOSE_HIGHLIGHTER_PATH)
}

class CodeStyleTest {
    @Test
    fun `expression-bodied when declarations should keep = and when on the same line`() {
        projectFileRoster.assertTrue { file ->
            val violation = file.text.lines()
                .zipWithNext()
                .withIndex()
                .firstOrNull { (_, linePair) ->
                    linePair.first.trimEnd().endsWith("=") &&
                        linePair.second.trimStart().startsWith("when {")
                }

            if (violation != null) {
                val lineNumber = violation.index + 1
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split '= when' declaration at line $lineNumber."
                )
            }

            violation == null
        }
    }

    @Test
    fun `expression-bodied declarations should keep = call on same line when it fits`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitExpressionBodyOpeningCall(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split '= call' declaration at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `governed kotlin files should not exceed hard wrap`() {
        hardWrapFileRoster.assertTrue { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                line.length > HARD_WRAP &&
                    isAllowedHardWrapTestName(line).not()
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Hard wrap exceeded at line ${violation.index + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `gradle test dependencies should use version catalog aliases`() {
        val violation = locateRepoRoot()
            .walkTopDown()
            .filter { file -> file.isFile && file.name.endsWith(".gradle.kts") }
            .filter { file -> file.path.contains(
                other = "${File.separator}build${File.separator}").not()
            }
            .firstOrNull { file -> file.readText().contains("kotlin(\"test\")") }

        if (violation != null) {
            val msg = "Name: ${violation.name}. FAILED. " +
                "Use libs.kotlin.test instead of kotlin(\"test\")."
            println(msg)
        }

        assertTrue(violation == null)
    }

    @Test
    fun `governance const declarations should stay top level after imports`() {
        governanceFileRoster.assertTrue { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (lineIndex, _) ->
                isLateConstDeclaration(lineRoster = file.text.lines(), lineIndex = lineIndex)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Const declaration at line ${violation.index + 1} " +
                    "must stay top level after imports."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single return functions should use expression bodies when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSingleReturnBlockFunction(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster.getOrNull(lineIndex + 1).orEmpty(),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single return function at " +
                    "line ${violation + 1} should use an expression body."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `declarations should not wrap rhs when two-line form fits within 100 chars`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isShortWrappedDeclaration(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Short wrapped declaration at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `one or two argument calls should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val composeCallNameRoster = composeCallNameRoster(file.text)
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitOneOrTwoArgumentCall(
                    composeCallNameRoster = composeCallNameRoster,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster.getOrNull(lineIndex + 1),
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                    fourthLine = lineRoster.getOrNull(lineIndex + 3),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "One or two argument call at line ${violation + 1} should stay on one line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compose calls with several parameters should use one parameter per line`() {
        projectFileRoster.assertTrue { file ->
            val composeCallNameRoster = composeCallNameRoster(file.text)
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                isCompactComposeCallWithSeveralParameters(composeCallNameRoster, line)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compose call at line ${violation.index + 1} " +
                    "should use one parameter per line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `object mapping calls with several arguments should use multiline arguments`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = lineRoster.withIndex().firstOrNull { (_, line) ->
                isInlineExpressionBodyObjectMapping(line)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Object mapping call at line ${violation.index + 1} " +
                    "should use multiline arguments."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `short elvis expressions should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitShortElvisExpression(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Short Elvis expression at line ${violation + 1} should stay on one line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `if statements should keep opening condition on the same line when it fits`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitIfOpeningCondition(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Split if opening condition at line ${violation + 1} exceeds style rules."
                )
            }

            violation == null
        }
    }

    @Test
    fun `guard return series should be followed by one blank line before final return`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isMissingBlankLineAfterGuardReturns(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Missing blank line after guard returns at line ${violation + 1}."
                )
            }

            violation == null
        }
    }

    @Test
    fun `function bodies should not start with a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterFunctionOpeningBrace(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Function body starts with a blank line at line ${violation + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single collection calls should stay with short receivers when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitSingleCollectionCallAfterShortReceiver(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single collection call at line ${violation + 1} " +
                    "must stay with its short receiver."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `explicit lambda parameter collection calls may keep multiline bodies`() {
        val isViolation = isSplitSingleCollectionCallAfterShortReceiver(
            currentLine = "    val colorStops = gradientColorAndPosition",
            nextLine = "        .map { colorAndPosition ->",
            thirdLine = "            colorAndPosition.second to colorAndPosition.first",
        )

        assertTrue(isViolation.not())
    }

    @Test
    fun `when branches should not be separated by blank lines`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (1 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineBetweenWhenBranches(
                    previousLine = lineRoster[lineIndex - 1],
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "When branches must not be separated by blank line ${violation + 1}."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single expression when branches should not use braces`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 2)).firstOrNull { lineIndex ->
                isSingleExpressionWhenBranchWithBraces(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single expression when branch at line ${violation + 1} " +
                    "should not use braces."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single parameter class constructors should stay on one line when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitSingleParameterClassHeader(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Split single-parameter class header at " +
                    "line ${violation + 1} exceeds style rules."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers should be followed by one blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isMissingBlankLineAfterCompactClassHeader(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                println(
                    "Name: ${file.name}. FAILED. " +
                        "Missing blank line after compact class header at line ${violation + 1}."
                )
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers before single one-line members should not have blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 2)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactClassHeaderBeforeOneLineMember(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact class header at line ${violation + 1} " +
                    "must not have a blank line before first one-line member."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact class headers before non one-line members should keep a blank line`() {
        val lineRoster = listOf(
            "class ToastGateway(private val appState: AppStateDataSource) {",
            "    fun getFlow(): Flow<ToastConfig> = appState.toastConfigFlow",
            "",
            "    suspend fun show(config: ToastConfig) {",
        )

        val isViolation = isMissingBlankLineAfterCompactClassHeader(
            lineRoster = lineRoster,
            lineIndex = 0,
            currentLine = lineRoster[0],
            nextLine = lineRoster[1],
        )

        assertTrue(isViolation)
    }

    @Test
    fun `compact abstract class headers before abstract member should not be followed by a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactAbstractClassHeaderBeforeAbstractMember(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact abstract class header at line ${violation + 1} " +
                    "must not have a blank line before first abstract member."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact sealed headers without parent functions should not be followed by a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineAfterCompactSealedHeaderWithoutParentFunction(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact sealed header at line ${violation + 1} " +
                    "must not have a blank line when parent has no functions."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `compact enum headers should not be followed by a blank line before first entry`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until (lineRoster.lastIndex - 1)).firstOrNull { lineIndex ->
                isBlankLineAfterCompactEnumHeader(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster[lineIndex + 2],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Compact enum header at line ${violation + 1} " +
                    "must not have a blank line before first entry."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `companion objects should not start with a blank line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isBlankLineAfterCompanionObjectOpening(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Companion object at line ${violation + 1} " +
                    "must not start with a blank line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `companion default value declarations should use DEFAULT name`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isCompanionDefaultValueDeclaration(
                    lineRoster = lineRoster,
                    lineIndex = lineIndex,
                    currentLine = lineRoster[lineIndex],
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Default value at line ${violation + 1} must be named DEFAULT."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `call chains should use one-line form or one call per wrapped line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isCallChainViolation(lineRoster = lineRoster, lineIndex = lineIndex)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Call chain at line ${violation + 1} " +
                    "must be one line or one call per wrapped line."
                println(msg)
            }

            violation == null
        }
    }



    private fun isShortWrappedDeclaration(
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

    private fun isSplitOneOrTwoArgumentCall(
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

    private fun isDeclarationOpening(line: String): Boolean {
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

    private fun isCompactComposeCallWithSeveralParameters(
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

    private fun composeCallNameRoster(fileText: String): Set<String> {
        if (fileText.contains("@Composable").not()) return emptySet()

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
        return BUILT_IN_COMPOSE_CALL_NAME_ROSTER + localNameRoster
    }

    private fun hasSeveralTopLevelArguments(line: String): Boolean {
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

    private fun isComposeCallOpening(
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

    private fun isExpressionBodyMappingCallOpening(line: String): Boolean {
        val match = EXPRESSION_BODY_MAPPING_CALL_REGEX.find(line) ?: return false
        val parameterText = match.groupValues[1]
        return topLevelArgumentCount(parameterText) == 1
    }

    private fun isInlineExpressionBodyObjectMapping(line: String): Boolean {
        val match = EXPRESSION_BODY_MAPPING_CALL_REGEX.find(line) ?: return false
        val parameterText = match.groupValues[1]
        if (topLevelArgumentCount(parameterText) != 1) return false
        if (line.substring(match.range.last + 1).contains(")").not()) return false

        val argumentPart = line
            .substring(match.range.last + 1)
            .substringBeforeLast(")")
        return topLevelArgumentCount(argumentPart) > 1
    }

    private fun topLevelArgumentCount(argumentText: String): Int {
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

    private fun isSplitShortElvisExpression(
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

    private fun isSingleReturnBlockFunction(
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

    private fun isSplitExpressionBodyOpeningCall(
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

    private fun joinLine(currentLine: String, nextLine: String): String = compactWhitespace(
        "${currentLine.trimEnd()} ${nextLine.trimStart()}",
    )

    private fun compactWhitespace(value: String): String = value
        .replace(WHITESPACE_REGEX, " ")
        .trim()

    private fun isSplitIfOpeningCondition(
        currentLine: String,
        nextLine: String,
    ): Boolean {
        val currentLineTrimmed = currentLine.trimEnd()
        val nextLineTrimmed = nextLine.trimStart()
        if (currentLineTrimmed.endsWith("if (").not()) return false
        if (nextLineTrimmed.isBlank()) return false

        return joinLine(currentLine, nextLine).length <= HARD_WRAP
    }

    private fun isMissingBlankLineAfterGuardReturns(
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

    private fun isBlankLineAfterFunctionOpeningBrace(
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

    private fun isSplitSingleCollectionCallAfterShortReceiver(
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

    private fun hasExplicitLambdaParameter(line: String): Boolean =
        EXPLICIT_LAMBDA_PARAMETER_REGEX.containsMatchIn(line)

    private fun isBlankLineBetweenWhenBranches(
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

    private fun isSingleExpressionWhenBranchWithBraces(
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

    private fun isCallChainViolation(
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

    private fun isClosedNonChainCallFollowedByShortDotCall(
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

    private fun findOpeningCallLineAbove(
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

    private fun isShortExpressionBodyChainSplitAfterInlineCall(
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

    private fun hasExpressionBodyStartAbove(
        lineRoster: List<String>,
        lineIndex: Int,
    ): Boolean {
        val startIndex = maxOf(0, lineIndex - LOOKBACK_LINE_COUNT)
        return (startIndex..lineIndex).any { candidateIndex ->
            lineRoster[candidateIndex].trimEnd().endsWith("=")
        }
    }

    private fun isSplitSingleParameterClassHeader(
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

    private fun isMissingBlankLineAfterCompactClassHeader(
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

    private fun isBlankLineAfterCompactClassHeaderBeforeOneLineMember(
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

    private fun isSingleOneLineMemberCompactClass(
        lineRoster: List<String>,
        lineIndex: Int,
    ): Boolean = countTopLevelMember(lineRoster, lineIndex) == 1 &&
        isOneLineMemberAllowedAfterCompactHeader(lineRoster[lineIndex + 1])

    private fun countTopLevelMember(
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

    private fun isCompactClassHeader(line: String): Boolean {
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

    private fun isOneLineMemberAllowedAfterCompactHeader(line: String): Boolean {
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

    private fun isBlankLineAfterCompactSealedHeaderWithoutParentFunction(
        lineRoster: List<String>,
        lineIndex: Int,
        currentLine: String,
        nextLine: String,
    ): Boolean {
        val currentLineTrimmed = currentLine.trimEnd()
        if (currentLineTrimmed.startsWith("sealed class ").not() &&
            currentLineTrimmed.startsWith("internal sealed class ").not() &&
            currentLineTrimmed.startsWith("actual sealed class ").not()
        ) {
            return false
        }
        if (currentLineTrimmed.endsWith("{").not()) return false
        if (nextLine.isBlank().not()) return false

        return hasTopLevelFunctionInsideSealedBody(
            lineRoster = lineRoster,
            headerLineIndex = lineIndex,
        ).not()
    }

    private fun isBlankLineAfterCompanionObjectOpening(
        currentLine: String,
        nextLine: String,
    ): Boolean = currentLine.trimEnd() == "companion object {" && nextLine.isBlank()

    private fun isCompanionDefaultValueDeclaration(
        lineRoster: List<String>,
        lineIndex: Int,
        currentLine: String,
    ): Boolean {
        val currentLineTrimmed = currentLine.trimStart()
        val isDefaultValue = COMPANION_DEFAULT_VALUE_REGEX.containsMatchIn(currentLineTrimmed)
        if (isDefaultValue.not()) return false

        return isInsideCompanionObject(lineRoster = lineRoster, lineIndex = lineIndex)
    }

    private fun isInsideCompanionObject(
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
    private fun isBlankLineAfterCompactEnumHeader(
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

    private fun isBlankLineAfterCompactAbstractClassHeaderBeforeAbstractMember(
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

    private fun isInlineCallChainStartBeforeMultilineContinuation(
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

    private fun isWrappedChainLineWithMultipleCalls(line: String): Boolean {
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

    private fun hasTopLevelFunctionInsideSealedBody(
        lineRoster: List<String>,
        headerLineIndex: Int,
    ): Boolean {
        var depth = 1
        for (candidateIndex in (headerLineIndex + 1)..lineRoster.lastIndex) {
            val line = lineRoster[candidateIndex]
            val trimmedLine = line.trimStart()
            if (depth == 1 && trimmedLine.contains("fun ")) return true

            depth += line.count { it == '{' }
            depth -= line.count { it == '}' }
            if (depth == 0) return false
        }

        return false
    }

    private fun isAllowedHardWrapTestName(line: String): Boolean =
        line.trimStart().startsWith("fun `") && line.trimEnd().endsWith("`() {")

    private fun isLateConstDeclaration(
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

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")

}
