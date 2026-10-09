package com.velord.infrastructure.konsist.codestyle

import com.velord.infrastructure.konsist.ActiveProjectSource
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import java.io.File

private const val SETTINGS_GRADLE_FILE = "settings.gradle.kts"
private val WHITESPACE_REGEX = Regex("\\s+")

internal const val HARD_WRAP = 100
internal val projectFileRoster = ActiveProjectSource.fileRoster

internal fun <T> List<String>.withKotlinFileRoster(block: (List<KtFile>) -> T): T {
    val disposable = Disposer.newDisposable()
    try {
        val environment = KotlinCoreEnvironment.createForProduction(
            disposable,
            CompilerConfiguration(),
            EnvironmentConfigFiles.JVM_CONFIG_FILES,
        )
        val factory = KtPsiFactory(environment.project)
        return block(map(factory::createFile))
    } finally {
        Disposer.dispose(disposable)
    }
}

internal fun joinLine(currentLine: String, nextLine: String): String = compactWhitespace(
    "${currentLine.trimEnd()} ${nextLine.trimStart()}",
)

internal fun compactWhitespace(value: String): String = value
    .replace(WHITESPACE_REGEX, " ")
    .trim()

internal fun locateRepoRoot(): File = generateSequence(
    File(System.getProperty("user.dir")).absoluteFile,
) { currentDirectory ->
    currentDirectory.parentFile
}.firstOrNull { currentDirectory ->
    File(currentDirectory, SETTINGS_GRADLE_FILE).isFile
} ?: error("Cannot locate repo root from ${System.getProperty("user.dir")}")
