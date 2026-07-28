plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.datastore"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.serialization.json)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            // DataStore
            implementation(libs.androidx.datastore.core.okio)
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
