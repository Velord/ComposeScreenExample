import com.velord.buildlogic.task.GenerateAppStringResourcesTask

plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val bundledLocalizationFile = project(":core:core-resource").layout.projectDirectory.file(
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
        namespace = "com.velord.data.localization"
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateAppStringResources.flatMap { it.outputDirectory })
            dependencies {
                // Raw resources only. Localization behavior lives in this module.
                api(projects.core.coreResource)
                // Module Model
                implementation(projects.model)
                // Compose
                implementation(libs.compose.runtime)
                // Kotlin Serialization
                implementation(libs.kotlin.serialization.json)
                // Firebase KMP
                implementation("dev.gitlive:firebase-config:2.6.0")
                // Koin
                implementation(libs.koin.core)
                api(libs.koin.annotation)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp)
    add("kspAndroid", libs.koin.ksp)
    add("kspDesktop", libs.koin.ksp)
}

tasks.matching {
    it.name.startsWith("ksp") &&
        it.name != "kspCommonMainKotlinMetadata"
}.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}
