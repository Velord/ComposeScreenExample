package com.velord.buildlogic.util

import com.velord.buildlogic.model.BuildEnvironment
import com.velord.buildlogic.model.BuildType

private const val APP_MODULE_NAME = "app"
private const val ANDROID_MODULE_NAME = "android"
private val CAMEL_WORD_REGEX = Regex("[A-Z]?[a-z0-9]+|[A-Z]")
private val VARIANT_SENSITIVE_TASK_ROSTER = listOf(
    "assemble",
    "bundle",
    "build",
    "compile",
    "install",
)

internal object BuildConfigFlavorResolver {

    // Gradle keeps requested abbreviations in StartParameter. Token matching resolves unique
    // abbreviations while rejecting aggregate, incomplete, ambiguous, and multi-variant requests.
    fun resolve(taskNameRoster: List<String>): String? {
        val variantRoster = taskNameRoster
            .flatMap { taskName -> taskName.resolveVariantRoster() }
            .distinct()
        require(variantRoster.size <= 1) {
            "Build configuration can generate only one flavor per Gradle invocation: " +
                variantRoster.map(BuildConfigVariant::flavor)
        }

        val incompleteAppTask = taskNameRoster.firstOrNull { taskName ->
            taskName.isIncompleteAppBuildTask()
        }
        require(incompleteAppTask == null) {
            "Build configuration task '$incompleteAppTask' requires one Android variant. " +
                    "Run a task such as :app:assembleProductionRelease."
        }

        return variantRoster.singleOrNull()?.flavor
    }
}

private data class BuildConfigVariant(
    val environment: BuildEnvironment,
    val buildType: BuildType,
) {
    val flavor = environment.variantName(buildType)
}

private fun String.resolveVariantRoster(): List<BuildConfigVariant> {
    val wordRoster = taskName().camelWordRoster()
    val variantRoster = wordRoster
        .zipWithNext()
        .flatMap { (environmentWord, buildTypeWord) ->
            BuildEnvironment.entries.flatMap { environment ->
                BuildType.entries.mapNotNull { buildType ->
                    BuildConfigVariant(environment, buildType).takeIf {
                        environment.value.startsWith(environmentWord, ignoreCase = true) &&
                            buildType.value.startsWith(buildTypeWord, ignoreCase = true)
                    }
                }
            }
        }
        .distinct()
    require(variantRoster.size <= 1) {
        "Build configuration task '$this' resolves to multiple Android variants: " +
            variantRoster.map(BuildConfigVariant::flavor)
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
    val moduleName = segmentRoster.dropLast(1).lastOrNull()
    val isApp = { moduleName == APP_MODULE_NAME }
    val isAndroid = { moduleName == ANDROID_MODULE_NAME }
    val isAppOrAndroid = { isApp() || isAndroid() }
    return segmentRoster.size >= 2 && isAppOrAndroid()
}

private fun String.taskName(): String = removePrefix(":").substringAfterLast(":")

private fun String.camelWordRoster(): List<String> = CAMEL_WORD_REGEX
    .findAll(this)
    .map(MatchResult::value)
    .toList()
