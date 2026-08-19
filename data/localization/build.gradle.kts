plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.localization"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Module Core
            implementation(projects.core.coreResource)
            // Module Infrastructure
            implementation(projects.infrastructure.config)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.serialization.json)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            // Firebase
            implementation(libs.firebase.kit.remote.config)
            implementation(libs.firebase.kit.crashlytics)
        }
        androidMain.dependencies {
            // Firebase
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.config)
        }
        commonTest.dependencies {
            // Kotlin
            implementation(libs.kotlin.test)
        }

        named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

dependencies {
    // KSP
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
