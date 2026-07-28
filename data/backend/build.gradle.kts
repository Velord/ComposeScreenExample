plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.backend"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Kotlin Serialization
            implementation(libs.kotlin.serialization.json)
            // Ktor Network
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.serialization)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
        }

        androidMain.dependencies {
            // Ktor Engine
            implementation(libs.ktor.client.okhttp)
        }

        desktopMain.dependencies {
            // Ktor Engine
            implementation(libs.ktor.client.okhttp)
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
