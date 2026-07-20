package com.velord.buildlogic.util

import com.velord.buildlogic.model.BuildEnvironment
import com.velord.buildlogic.model.BuildType

private const val APP_MODULE_NAME = "app"
private val CAMEL_WORD_REGEX = Regex("[A-Z]?[a-z0-9]+|[A-Z]")
private val VARIANT_SENSITIVE_TASK_ROSTER = listOf(
    "assemble",
    "bundle",
    "build",
    "compile",
    "install",
)

internal object BuildKonfigFlavorResolver {

    // Gradle keeps requested abbreviations in StartParameter. Token matching resolves unique
    // abbreviations while rejecting aggregate, incomplete, ambiguous, and multi-variant requests.
    fun resolve(taskNameRoster: List<String>): String? {
        val variantRoster = taskNameRoster
            .flatMap { taskName -> taskName.resolveVariantRoster() }
            .distinct()
        require(variantRoster.size <= 1) {
            "BuildKonfig can generate only one flavor per Gradle invocation: " +
                variantRoster.map(BuildKonfigVariant::flavor)
        }

        val incompleteAppTask = taskNameRoster.firstOrNull { taskName ->
            taskName.isIncompleteAppBuildTask()
        }
        require(incompleteAppTask == null) {
            "BuildKonfig requires one Android variant per Gradle invocation. " +
                "Run a task such as :app:assembleProductionRelease."
        }
        return variantRoster.singleOrNull()?.flavor
    }
}

private data class BuildKonfigVariant(
    val environment: BuildEnvironment,
    val buildType: BuildType,
) {
    val flavor = environment.variantName(buildType)
}

private fun String.resolveVariantRoster(): List<BuildKonfigVariant> {
    val wordRoster = taskName().camelWordRoster()
    val variantRoster = wordRoster
        .zipWithNext()
        .flatMap { (environmentWord, buildTypeWord) ->
            BuildEnvironment.entries.flatMap { environment ->
                BuildType.entries.mapNotNull { buildType ->
                    BuildKonfigVariant(environment, buildType).takeIf {
                        environment.value.startsWith(environmentWord, ignoreCase = true) &&
                            buildType.value.startsWith(buildTypeWord, ignoreCase = true)
                    }
                }
            }
        }
        .distinct()
    require(variantRoster.size <= 1) {
        "BuildKonfig task '$this' resolves to multiple Android variants: " +
            variantRoster.map(BuildKonfigVariant::flavor)
    }
    return variantRoster
}

private fun String.isIncompleteAppBuildTask(): Boolean {
    if (isAppTask().not() || resolveVariantRoster().isNotEmpty()) return false

    val wordRoster = taskName().camelWordRoster()
    val hasVariantSensitiveOperation = VARIANT_SENSITIVE_TASK_ROSTER.any { operation ->
        wordRoster.firstOrNull()?.let { operation.startsWith(it, ignoreCase = true) } == true
    }
    val hasBuildType = wordRoster.any { word ->
        BuildType.entries.any { buildType -> buildType.value.startsWith(word, ignoreCase = true) }
    }
    return hasVariantSensitiveOperation || hasBuildType
}

private fun String.isAppTask(): Boolean {
    val segmentRoster = removePrefix(":").split(":")

    return segmentRoster.size == 1 || segmentRoster.dropLast(1).lastOrNull() == APP_MODULE_NAME
}

private fun String.taskName(): String = removePrefix(":").substringAfterLast(":")

private fun String.camelWordRoster(): List<String> = CAMEL_WORD_REGEX.findAll(this)
    .map(MatchResult::value)
    .toList()
