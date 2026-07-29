plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.os"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Infrastructure
            implementation(projects.infrastructure.config)
            // Module Model
            implementation(projects.model)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            // Kamera
            implementation(libs.kamera.core)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            // Logging
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            // AndroidX
            implementation(libs.androidx.lifecycle.process)
            // Kamera
            implementation(libs.kamera.core)
            // Firebase
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.crashlytic)
        }

        desktopMain.dependencies {
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            // Kamera
            implementation(libs.kamera.core)
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

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
