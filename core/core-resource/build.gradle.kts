import com.velord.buildlogic.task.GenerateAppStringResourcesTask

plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.velord.core.resource"
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

kotlin {
    android {
        namespace = "com.velord.core.resource"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateAppStringResources.flatMap { it.outputDirectory })
            dependencies {
                api(projects.model)
                api(libs.compose.resources)
                implementation(libs.compose.runtime)
            }
        }
    }
}
