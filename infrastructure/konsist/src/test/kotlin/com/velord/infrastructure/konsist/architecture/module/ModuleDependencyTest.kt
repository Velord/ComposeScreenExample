package com.velord.infrastructure.konsist.architecture.module

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.declaration.KoFunctionDeclaration
import com.lemonappdev.konsist.api.declaration.KoPropertyDeclaration
import com.lemonappdev.konsist.api.declaration.KoTypeAliasDeclaration
import com.lemonappdev.konsist.api.declaration.combined.KoClassAndInterfaceAndObjectDeclaration
import com.lemonappdev.konsist.api.ext.koscope.declarationsOf
import com.lemonappdev.konsist.api.provider.KoModuleProvider
import com.lemonappdev.konsist.api.provider.KoReceiverTypeProvider
import com.lemonappdev.konsist.api.provider.KoSourceSetProvider
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

private const val UI_FOLDER_NAME = "ui"
private const val DATA_GATEWAY_COMMON_PATH = "data/gateway/src/commonMain/kotlin"
private const val INFRASTRUCTURE_DI_PATH = "infrastructure/di/src"
private const val BUILD_FILE_NAME = "build.gradle.kts"
private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private const val KAMERA_IMPORT_PREFIX = "import com.kashif.cameraK."
private val DI_MODULE_FILE_REGEX = Regex(
    """.*[\\/]infrastructure[\\/]di[\\/]src[\\/][^\\/]+Main[\\/].*Module\.kt$""",
)
private val PLATFORM_MODULE_FILE_REGEX = Regex(
    """.*[\\/]infrastructure[\\/]di[\\/]src[\\/][^\\/]+Main[\\/].*PlatformModule\.kt$""",
)
private val USE_CASE_MODULE_FILE_REGEX = Regex(
    """.*[\\/]infrastructure[\\/]di[\\/]src[\\/]commonMain[\\/].*UseCaseModule\.kt$""",
)
private val DATA_PROJECT_ACCESSOR_REGEX = Regex("""projects\.data\.[A-Za-z][A-Za-z0-9]*""")
private val DATA_PROJECT_PATH_REGEX = Regex("""project\(\s*[\"']:data:[^\"']+[\"']\s*\)""")
private val KAMERA_ALIAS_REGEX = Regex("""\s+as\s+[A-Za-z0-9]*[Kk]amera[A-Za-z0-9]*$""")
private val APP_EVENT_REFERENCE_REGEX = Regex("""\bAppEvent\.""")
private val COLLABORATOR_CONTRACT_REGEX = Regex(
    """\b(?:fun\s+)?interface\s+([A-Za-z][A-Za-z0-9]*(?:Reader|Updater|Manager))\b""",
)
private val GATEWAY_CLASS_CONSTRUCTOR_REGEX = Regex(
    """class\s+([A-Za-z][A-Za-z0-9]*Gateway)\s*\((.*?)\)\s*(?::|\{)""",
    setOf(RegexOption.DOT_MATCHES_ALL),
)
private val CONSTRUCTOR_TYPE_REGEX = Regex(
    """(?:private\s+)?(?:val|var)\s+[A-Za-z][A-Za-z0-9]*\s*:\s*([A-Za-z][A-Za-z0-9]*)""",
)
private val DI_GATEWAY_DECLARATION_REGEX = Regex(
    """\b(?:class|interface|fun\s+interface|object)\s+([A-Za-z][A-Za-z0-9]*Gateway)\b""",
)
private val USE_CASE_CONSTRUCTOR_CALL_REGEX = Regex("""\b[A-Za-z][A-Za-z0-9]*UC\s*\(""")
private val USE_CASE_CONSTRUCTOR_LAMBDA_REGEX = Regex("""\b[A-Za-z][A-Za-z0-9]*UC\s*\{""")
private val USE_CASE_IMPORT_REGEX = Regex("""(?m)^import\s+com\.velord\.usecase\.""")
private val USE_CASE_BINDING_REGEX = Regex("""single<\s*[A-Za-z0-9_]+UC\s*>""")
private val SAME_TARGET_EXTENSION_DEBT_ROSTER = emptySet<String>()

class ModuleDependencyTest {

    private val repoRoot = locateRepoRoot()
    private val projectScope = Konsist.scopeFromProject()

    @Test
    fun `ui modules should not depend directly on data modules`() {
        val violationRoster = File(repoRoot, UI_FOLDER_NAME)
            .walkTopDown()
            .filter { file -> file.name == BUILD_FILE_NAME }
            .flatMap { file ->
                file.readLines().withIndex().mapNotNull { (lineIndex, line) ->
                    line.takeIf(::isDirectDataDependency)?.let {
                        "${file.relativeTo(repoRoot).path}:${lineIndex + 1}"
                    }
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "UI modules depend directly on data modules: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `project owned types should not have extensions in their owning target`() {
        val functionViolationRoster = projectScope
            .declarationsOf<KoFunctionDeclaration>()
            .filter { function -> function.hasSameModuleReceiver() }
            .map { function -> "${function.containingFile.name}:${function.name}" }
        val propertyViolationRoster = projectScope
            .declarationsOf<KoPropertyDeclaration>()
            .filter { property -> property.hasSameModuleReceiver() }
            .map { property -> "${property.containingFile.name}:${property.name}" }
        val violationRoster = (functionViolationRoster + propertyViolationRoster).toSet()
        val unexpectedViolationRoster = violationRoster - SAME_TARGET_EXTENSION_DEBT_ROSTER
        val staleDebtRoster = SAME_TARGET_EXTENSION_DEBT_ROSTER - violationRoster

        if (unexpectedViolationRoster.isNotEmpty()) {
            val msg = "Unexpected same-target extensions: " +
                unexpectedViolationRoster.joinToString()
            println(msg)
        }
        if (staleDebtRoster.isNotEmpty()) {
            val msg = "Remove migrated same-target extension debt: " +
                staleDebtRoster.joinToString()
            println(msg)
        }

        assertTrue(unexpectedViolationRoster.isEmpty())
        assertTrue(staleDebtRoster.isEmpty())
    }

    @Test
    fun `kamera imports should use library prefixed aliases`() {
        val violationRoster = projectScope.files.flatMap { file ->
            file.text.lines().withIndex().mapNotNull { (lineIndex, line) ->
                val isKameraImport = line.startsWith(KAMERA_IMPORT_PREFIX)
                val hasLibraryAlias = KAMERA_ALIAS_REGEX.containsMatchIn(line)
                if (isKameraImport && hasLibraryAlias.not()) {
                    "${file.name}:${lineIndex + 1}"
                } else {
                    null
                }
            }
        }

        if (violationRoster.isNotEmpty()) {
            val msg = "Kamera imports without library-prefixed aliases: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `data gateway should not define public collaborator contracts`() {
        val dataGatewayRoot = File(repoRoot, DATA_GATEWAY_COMMON_PATH)
        val violationRoster = dataGatewayRoot
            .walkTopDown()
            .filter { file -> file.extension == "kt" }
            .flatMap { file ->
                COLLABORATOR_CONTRACT_REGEX.findAll(file.readText()).map { match ->
                    val contractName = match.groupValues[1]
                    val lineNumber = file.lineNumberOf(match.range.first)
                    "${file.relativeTo(repoRoot).path}:$lineNumber:$contractName"
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "Data gateway defines collaborator contracts: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `gateway constructors should depend only on data sources or gateways`() {
        val dataGatewayRoot = File(repoRoot, DATA_GATEWAY_COMMON_PATH)
        val violationRoster = dataGatewayRoot
            .walkTopDown()
            .filter { file -> file.extension == "kt" }
            .flatMap { file ->
                GATEWAY_CLASS_CONSTRUCTOR_REGEX.findAll(file.readText()).flatMap { match ->
                    val gatewayName = match.groupValues[1]
                    val constructorText = match.groupValues[2]
                    CONSTRUCTOR_TYPE_REGEX.findAll(constructorText).mapNotNull { parameter ->
                        val typeName = parameter.groupValues[1]
                        if (typeName.endsWith("Gateway") || typeName.endsWith("DataSource")) {
                            null
                        } else {
                            "${file.relativeTo(repoRoot).path}:$gatewayName:$typeName"
                        }
                    }
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "Gateway constructors depend on non-source collaborators: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `di module should not declare gateways`() {
        val diRoot = File(repoRoot, INFRASTRUCTURE_DI_PATH)
        val violationRoster = diRoot
            .walkTopDown()
            .filter { file -> file.extension == "kt" }
            .flatMap { file ->
                DI_GATEWAY_DECLARATION_REGEX.findAll(file.readText()).map { match ->
                    val gatewayName = match.groupValues[1]
                    val lineNumber = file.lineNumberOf(match.range.first)
                    "${file.relativeTo(repoRoot).path}:$lineNumber:$gatewayName"
                }
            }
            .toList()

        if (violationRoster.isNotEmpty()) {
            val msg = "DI module declares gateway types: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `di modules should bind use cases to callable references`() {
        val violationRoster = projectScope.files
            .filter { file -> DI_MODULE_FILE_REGEX.matches(file.path) }
            .flatMap { file ->
                file.text.lines().withIndex().mapNotNull { (lineIndex, line) ->
                    val hasLambdaConstructor = USE_CASE_CONSTRUCTOR_LAMBDA_REGEX
                        .containsMatchIn(line)
                    val hasCallConstructor = USE_CASE_CONSTRUCTOR_CALL_REGEX
                        .containsMatchIn(line)
                    val hasCallableReference = line.contains("::")
                    if (hasLambdaConstructor ||
                        hasCallConstructor && hasCallableReference.not()
                    ) {
                        "${file.name}:${lineIndex + 1}"
                    } else {
                        null
                    }
                }
            }

        if (violationRoster.isNotEmpty()) {
            val msg = "DI modules bind use cases without callable references: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `use case module should not construct app events`() {
        val violationRoster = projectScope.files
            .filter { file -> USE_CASE_MODULE_FILE_REGEX.matches(file.path) }
            .flatMap { file ->
                file.text.lines().withIndex().mapNotNull { (lineIndex, line) ->
                    if (APP_EVENT_REFERENCE_REGEX.containsMatchIn(line)) {
                        "${file.name}:${lineIndex + 1}"
                    } else {
                        null
                    }
                }
            }

        if (violationRoster.isNotEmpty()) {
            val msg = "Use case module constructs app events: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    @Test
    fun `platform modules should not bind domain use cases`() {
        val violationRoster = projectScope.files
            .filter { file -> PLATFORM_MODULE_FILE_REGEX.matches(file.path) }
            .flatMap { file ->
                val useCaseImportViolation = USE_CASE_IMPORT_REGEX.find(file.text)
                    ?.let { match -> "${file.name}:${file.lineNumberOf(match.range.first)}" }
                val useCaseBindingViolation = USE_CASE_BINDING_REGEX.find(file.text)
                    ?.let { match -> "${file.name}:${file.lineNumberOf(match.range.first)}" }

                listOfNotNull(useCaseImportViolation, useCaseBindingViolation)
            }

        if (violationRoster.isNotEmpty()) {
            val msg = "Platform modules bind domain use cases: " +
                violationRoster.joinToString()
            println(msg)
        }

        assertTrue(violationRoster.isEmpty())
    }

    private fun isDirectDataDependency(line: String): Boolean {
        if (line.trimStart().startsWith("//")) return false

        return DATA_PROJECT_ACCESSOR_REGEX.containsMatchIn(line) ||
            DATA_PROJECT_PATH_REGEX.containsMatchIn(line)
    }

    private fun KoReceiverTypeProvider.hasSameModuleReceiver(): Boolean {
        val declarationModule = this as KoModuleProvider
        val receiverDeclaration = receiverType?.sourceDeclaration ?: return false
        val isProjectOwnedType = receiverDeclaration is KoClassAndInterfaceAndObjectDeclaration ||
            receiverDeclaration is KoTypeAliasDeclaration
        if (isProjectOwnedType.not()) return false

        val receiverModule = receiverDeclaration as KoModuleProvider
        if (declarationModule.moduleName != receiverModule.moduleName) return false

        val declarationSourceSet = (this as KoSourceSetProvider).sourceSetName
        val receiverSourceSet = (receiverDeclaration as KoSourceSetProvider).sourceSetName
        return declarationSourceSet == receiverSourceSet
    }

    private fun locateRepoRoot(): File = generateSequence(
        File(System.getProperty("user.dir")).absoluteFile,
    ) { currentDirectory ->
        currentDirectory.parentFile
    }.firstOrNull { currentDirectory ->
        File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
    } ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")

    private fun File.lineNumberOf(index: Int): Int =
        readText().take(index).count { character -> character == '\n' } + 1

    private fun KoFileDeclaration.lineNumberOf(
        index: Int
    ): Int = text.take(index).count { character -> character == '\n' } + 1
}
