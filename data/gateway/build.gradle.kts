plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.gateway"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Module Data
            implementation(projects.data.appstate)
            implementation(projects.data.backend)
            implementation(projects.data.datastore)
            implementation(projects.data.db)
            implementation(projects.data.os)
            // Module Domain
            implementation(projects.domain.usecaseMovie)
            implementation(projects.domain.usecaseCamera)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            // Logging
            implementation(libs.kermit)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
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
