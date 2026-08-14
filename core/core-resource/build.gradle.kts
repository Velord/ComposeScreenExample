import com.velord.buildlogic.task.GenerateAppStringResourcesTask

plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val bundledLocalizationFile = layout.projectDirectory.file(
    "src/commonMain/composeResources/files/localization.json",
)
val generatedLocalizationDirectory = layout.buildDirectory.dir(
    "generated/localization/commonMain/kotlin",
)
val generateAppStringResources = tasks.register<GenerateAppStringResourcesTask>(
    "generateAppStringResources",
) {
    localizationFile.set(bundledLocalizationFile)
    outputDirectory.set(generatedLocalizationDirectory)
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
