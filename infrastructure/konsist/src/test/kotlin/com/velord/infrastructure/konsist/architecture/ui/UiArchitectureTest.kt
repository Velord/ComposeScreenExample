package com.velord.infrastructure.konsist.architecture.ui

import com.lemonappdev.konsist.api.Konsist
import com.velord.infrastructure.konsist.codestyle.HARD_WRAP
import com.velord.infrastructure.konsist.codestyle.compactWhitespace
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val INLINE_UI_ACTION_BRANCH_EXTRA_INDENT = 8
private const val SCREEN_FILE_SUFFIX = "Screen.kt"
private const val UI_FEATURE_IMPORT_PREFIX = "import com.velord.ui.feature."
private const val COMPONENT_IMPORT_SEGMENT = ".component."
private const val CONTENT_NAME = "Content"
private const val CONTENT_FILE_NAME = "Content.kt"
private const val LIFECYCLE_COLLECTION = "collectAsStateWithLifecycle"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private const val UI_PACKAGE_PREFIX = "com.velord.ui."
private const val ANDROID_TOAST_TYPE = "android.widget.Toast"
private const val TOAST_CONFIG_TYPE = "ToastConfig"
private const val UI_ACTION_SUFFIX = "UiAction"
private const val UI_CONTRACT_EXEMPT_ANNOTATION = "@UiContractExempt("
private const val UI_STATE_FLOW_PROPERTY = "val uiStateFlow"
private const val UI_STATE_SUFFIX = "UiState"
private const val VIEW_MODEL_SUFFIX = "VM"
private val HARDCODED_MESSAGE_REGEX =
    Regex("""(?:\bval\s+\w*[Mm]essage\w*\s*=\s*|message\s*=\s*)"[^"]+"""")
private val DIRECT_VIEW_MODEL_COLLECTION_REGEX =
    Regex("""\bviewModel\.[A-Za-z][A-Za-z0-9]*\.collectAsStateWithLifecycle\(""")
private val MUTABLE_STATE_FLOW_DECLARATION_REGEX =
    Regex("""\bval\s+[A-Za-z][A-Za-z0-9]*\s*(?::[^=]+)?=\s*MutableStateFlow""")
private val UI_ENTRY_REGEX = Regex("""(?m)^\s*(?:public\s+)?fun\s+on[A-Z][A-Za-z0-9]*\s*\(""")
private val UI_ACTION_BRANCH_REGEX =
    Regex("""(?:is\s+)?[A-Za-z][A-Za-z0-9]*UiAction\.([A-Za-z][A-Za-z0-9]*)\s*->""")
private val UI_ACTION_DELEGATE_REGEX = Regex(
    """(?:is\s+)?[A-Za-z][A-Za-z0-9]*UiAction\.([A-Za-z][A-Za-z0-9]*)\s*->""" +
        """\s*([A-Za-z][A-Za-z0-9]*)\s*\("""
)
private val UI_ACTION_HANDLER_REGEX =
    Regex("""private\s+fun\s+handleUiAction\s*\(\s*action\s*:\s*\w+UiAction\s*\)\s*\{""")
private val UI_ACTION_COLLECTION_DELEGATE_REGEX = Regex(
    """actionFlow\.collect(?:Latest)?\s*\{\s*action\s*->""" +
        """\s*handleUiAction\(action\)\s*}"""
)
private val UI_ACTION_INLINE_DISPATCH_REGEX = Regex(
    """actionFlow\.collect(?:Latest)?\s*\{\s*(?:\w+\s*->\s*)?""" +
        """when\s*\(\s*\w+\s*\)\s*\{"""
)
private val UI_ACTION_COLLECTION_REGEX = Regex("""actionFlow\.collect(?:Latest)?\s*\{""")

class UiArchitectureTest {

    private val projectFileRoster = Konsist.scopeFromExternalDirectory(locateRepoRoot().path).files

    @Test
    fun `screen lifecycle state collection should be direct from view model`() {
        screenFileRoster().forEach { file ->
            val violation = file.text.lines().withIndex().firstOrNull { (_, line) ->
                isLifecycleCollection(line) &&
                    DIRECT_VIEW_MODEL_COLLECTION_REGEX.containsMatchIn(line).not()
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Lifecycle state collection at line ${violation.index + 1} " +
                    "must be direct from viewModel."
                println(msg)
            }

            assertTrue(violation == null)
        }
    }

    @Test
    fun `ui should not use Android Toast directly`() {
        val violationRoster = projectFileRoster.filter { file ->
            file.packagee?.name?.startsWith(UI_PACKAGE_PREFIX) == true &&
                file.text.lines().any(::containsAndroidToastUsage)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "UI must emit toast through ShowToastUC: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `toast messages should come from resources`() {
        val violationRoster = projectFileRoster.filter { file ->
            file.packagee?.name?.startsWith(UI_PACKAGE_PREFIX) == true &&
                file.text.contains(TOAST_CONFIG_TYPE) &&
                HARDCODED_MESSAGE_REGEX.containsMatchIn(file.text)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Toast messages must come from resources: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `view models with ui entry methods should use UiAction`() {
        val violationRoster = viewModelFileRoster().filter { file ->
            val hasInvalidContract = hasUiContractExemption(file.text) ||
                hasMatchingUiActionContract(file.name, file.text).not()
            hasPublicUiEntry(file.text) && hasInvalidContract
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "VM UI entry methods must use matching UiAction: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `view models with mutable presentation state should use UiState`() {
        val violationRoster = viewModelFileRoster().filter { file ->
            hasMutablePresentationState(file.text) &&
                hasUiContractExemption(file.text).not() &&
                hasMatchingUiStateContract(file.name, file.text).not()
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "VM mutable presentation state must use matching UiState: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `ui action dispatch should extract only when inline branches exceed hard wrap`() {
        val violationRoster = viewModelFileRoster().filter { file ->
            hasUiActionCollection(file.text) &&
                hasInvalidUiActionDispatch(file.text)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "UiAction dispatch must stay inline unless a branch exceeds hard wrap: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `ui action branches should delegate to matching handlers`() {
        val violationRoster = viewModelFileRoster().filter { file ->
            hasInvalidUiActionBranch(file.text)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "UiAction branches must delegate to matching private handlers: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `ui contract exemptions should remain simple`() {
        val violationRoster = viewModelFileRoster().filter { file ->
            hasUiContractExemption(file.text) &&
                (hasPublicUiEntry(file.text) || mutableStateFlowCount(file.text) > 1)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "UiContractExempt VMs must remain base or simple-flow VMs: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `extracted screen root composable should use Content name`() {
        val violationRoster = screenFileRoster().filter { file ->
            file.text.lineSequence().any { line ->
                isFeaturePrefixedRootContentImport(line)
            }
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Extracted screen root composable must use Content name: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `screen Content should not expose Modifier`() {
        val violationRoster = contentOwnerFileRoster().filter { file ->
            hasContentModifier(file.text)
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Screen Content must own its root layout without Modifier: " +
                violationRoster.joinToString { file -> file.name }
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    private fun screenFileRoster() = projectFileRoster.filter { file ->
        file.name.endsWith(SCREEN_FILE_SUFFIX)
    }

    private fun contentOwnerFileRoster() = projectFileRoster.filter { file ->
        file.name.endsWith(SCREEN_FILE_SUFFIX) || file.name == CONTENT_FILE_NAME
    }

    private fun viewModelFileRoster() = projectFileRoster.filter { file ->
        file.name.endsWith(VIEW_MODEL_SUFFIX)
    }

    private fun hasPublicUiEntry(text: String): Boolean = UI_ENTRY_REGEX.containsMatchIn(text)

    private fun hasUiContractExemption(text: String): Boolean =
        text.contains(UI_CONTRACT_EXEMPT_ANNOTATION)

    private fun hasMatchingUiActionContract(fileName: String, text: String): Boolean {
        val actionName = fileName.removeSuffix(VIEW_MODEL_SUFFIX) + UI_ACTION_SUFFIX
        val hasOnAction = Regex("""fun\s+onAction\(action:\s*$actionName\)""").containsMatchIn(text)
        val hasActionType = projectFileRoster.any { file ->
            val actionRegex = Regex("""(?:sealed\s+)?(?:interface|class)\s+$actionName\b""")
            actionRegex.containsMatchIn(file.text)
        }

        return hasOnAction && hasActionType
    }

    private fun hasMutablePresentationState(text: String): Boolean = mutableStateFlowCount(text) > 0

    private fun mutableStateFlowCount(text: String): Int =
        MUTABLE_STATE_FLOW_DECLARATION_REGEX.findAll(text).count()

    private fun hasMatchingUiStateContract(fileName: String, text: String): Boolean {
        val stateName = fileName.removeSuffix(VIEW_MODEL_SUFFIX) + UI_STATE_SUFFIX
        val hasStateFlow = text.contains(UI_STATE_FLOW_PROPERTY) && text.contains(stateName)
        val hasStateType = projectFileRoster.any { file ->
            Regex("""(?:data\s+)?(?:class|interface)\s+$stateName\b""").containsMatchIn(file.text)
        }

        return hasStateFlow && hasStateType
    }

    private fun hasUiActionCollection(text: String): Boolean = text.contains("actionFlow.collect")

    private fun hasInvalidUiActionDispatch(text: String): Boolean {
        val code = text.lines().joinToString("\n") { line -> line.substringBefore("//") }
        val inlineIndent = inlineUiActionBranchIndent(code)
        val mustExtract = uiActionBranchRoster(code).any { branch ->
            inlineIndent + branch.length > HARD_WRAP
        }
        val hasHandler = UI_ACTION_HANDLER_REGEX.containsMatchIn(code)
        val hasDelegatedCollection = UI_ACTION_COLLECTION_DELEGATE_REGEX.containsMatchIn(code)
        val hasInlineDispatch = UI_ACTION_INLINE_DISPATCH_REGEX.containsMatchIn(code)

        return if (mustExtract) {
            hasHandler.not() || hasDelegatedCollection.not()
        } else {
            hasHandler || hasInlineDispatch.not()
        }
    }

    private fun inlineUiActionBranchIndent(text: String): Int {
        val collectorLine = text.lineSequence().first { line ->
            UI_ACTION_COLLECTION_REGEX.containsMatchIn(line)
        }
        val collectorIndent = collectorLine.indexOfFirst { char -> char.isWhitespace().not() }

        return collectorIndent + INLINE_UI_ACTION_BRANCH_EXTRA_INDENT
    }

    private fun uiActionBranchRoster(text: String): List<String> {
        val lineRoster = text.lines()
        return lineRoster.indices.mapNotNull { lineIndex ->
            val line = lineRoster[lineIndex]
            if (UI_ACTION_BRANCH_REGEX.containsMatchIn(line).not()) return@mapNotNull null

            collectUiActionBranch(lineRoster, lineIndex)
        }
    }

    private fun collectUiActionBranch(lineRoster: List<String>, startIndex: Int): String {
        val branchPartRoster = mutableListOf<String>()
        var parenthesisDepth = 0
        var isCallStarted = false

        for (lineIndex in startIndex until lineRoster.size) {
            val line = lineRoster[lineIndex]
            if (lineIndex > startIndex && UI_ACTION_BRANCH_REGEX.containsMatchIn(line)) break

            val linePart = if (lineIndex == startIndex) {
                line.trim().substringAfter("->")
            } else {
                line.trim()
            }
            if (lineIndex == startIndex) {
                branchPartRoster += line.trim().substringBefore("->") + "->"
            }
            branchPartRoster += linePart

            for (char in linePart) {
                if (char == '(') {
                    isCallStarted = true
                    parenthesisDepth++
                }
                if (char == ')') parenthesisDepth--
            }
            if (isCallStarted && parenthesisDepth == 0) break
        }

        return compactWhitespace(branchPartRoster.joinToString(" "))
    }

    private fun hasInvalidUiActionBranch(text: String): Boolean {
        val code = text.lines().joinToString("\n") { line -> line.substringBefore("//") }
        val branchRoster = UI_ACTION_BRANCH_REGEX.findAll(code).toList()
        val delegateRoster = UI_ACTION_DELEGATE_REGEX.findAll(code).toList()
        if (branchRoster.size != delegateRoster.size) return true

        return delegateRoster.any { match ->
            val actionName = match.groupValues[1]
            val handlerName = match.groupValues[2]
            val handlerActionName = if (hasLegacyOnPrefix(actionName)) {
                actionName.removePrefix("On")
            } else {
                actionName
            }
            val expectedHandlerName = "on$handlerActionName"
            handlerName != expectedHandlerName || hasPrivateHandler(code, handlerName).not()
        }
    }

    private fun hasLegacyOnPrefix(actionName: String): Boolean =
        actionName.startsWith("On") && actionName.getOrNull(2)?.isUpperCase() == true

    private fun hasPrivateHandler(text: String, handlerName: String): Boolean =
        Regex("""private\s+fun\s+$handlerName\s*\(""").containsMatchIn(text)

    private fun isLifecycleCollection(line: String): Boolean {
        val lineTrimmed = line.trimStart()
        return lineTrimmed.startsWith("import ").not() &&
            line.contains(LIFECYCLE_COLLECTION)
    }

    private fun containsAndroidToastUsage(line: String): Boolean {
        val code = line.substringBefore("//")
        return code.contains(ANDROID_TOAST_TYPE)
    }

    private fun isFeaturePrefixedRootContentImport(line: String): Boolean {
        val importedName = line
            .takeIf { it.startsWith("import ") }
            ?.substringAfterLast('.')
            ?.substringBefore(" as ")
            ?: return false

        return line.startsWith(UI_FEATURE_IMPORT_PREFIX) &&
            line.contains(COMPONENT_IMPORT_SEGMENT) &&
            importedName.endsWith(CONTENT_NAME) &&
            importedName != CONTENT_NAME
    }

    private fun hasContentModifier(text: String): Boolean {
        val lineRoster = text.lines()
        return lineRoster.indices.any { lineIndex ->
            val line = lineRoster[lineIndex]
            if (line.contains("fun Content(").not()) return@any false

            buildString {
                for (declarationLine in lineRoster.drop(lineIndex)) {
                    appendLine(declarationLine)
                    if (declarationLine.trimEnd().endsWith(") {")) break
                }
            }.contains("modifier: Modifier")
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
