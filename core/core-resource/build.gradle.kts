import groovy.json.JsonSlurper

private fun String.toKotlinStringLiteral(): String = replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("$", "\\$")
    .replace("\r", "\\r")
    .replace("\n", "\\n")

plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val localizationFile = layout.projectDirectory.file(
    "src/commonMain/composeResources/files/localization.json",
)
val generatedLocalizationDirectory = layout.buildDirectory.dir(
    "generated/localization/commonMain/kotlin",
)
val generateAppStringResources = tasks.register("generateAppStringResources") {
    inputs.file(localizationFile)
    outputs.dir(generatedLocalizationDirectory)

    doLast {
        val document = JsonSlurper().parse(localizationFile.asFile) as Map<*, *>
        val languages = document["languages"] as? Map<*, *>
            ?: error("localization.json must contain languages")
        val english = languages["en"] as? Map<*, *>
            ?: error("localization.json must contain languages.en")
        val outputDirectory = generatedLocalizationDirectory.get().asFile
        val outputFile = outputDirectory.resolve(
            "com/velord/core/resource/AppString.kt",
        )
        val entryRoster = english.entries
            .map { it.key.toString() to it.value.toString() }
            .sortedBy { it.first }

        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            buildString {
                appendLine("package com.velord.core.resource")
                appendLine()
                appendLine("@JvmInline")
                appendLine("value class AppStringResource internal constructor(")
                appendLine("    internal val key: String,")
                appendLine("    internal val defaultValue: String,")
                appendLine(")")
                appendLine()
                appendLine("object AppString {")
                entryRoster.forEach { (key, value) ->
                    append("    val $key = AppStringResource(")
                    append(key.toKotlinStringLiteral())
                    append(", ")
                    append(value.toKotlinStringLiteral())
                    appendLine(")")
                }
                appendLine("}")
            },
        )
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.velord.core.resource"
}

kotlin {
    android {
        namespace = "com.velord.core.resource"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generatedLocalizationDirectory)
            dependencies {
                // Module Model
                implementation(projects.model)
                // Compose
                api(libs.compose.resources)
                implementation(libs.compose.runtime)
                // Kotlin Serialization
                implementation(libs.kotlin.serialization.json)
            }
        }
        commonTest.dependencies {
            // Testing
            implementation(libs.kotlin.test)
        }
    }
}

tasks.matching {
    it.name.startsWith("compile") && it.name.contains("Kotlin")
}.configureEach {
    dependsOn(generateAppStringResources)
}
