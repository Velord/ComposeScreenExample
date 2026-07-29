plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    android {
        namespace = "com.velord.data.db"
    }

    sourceSets {
        commonMain.dependencies {
            // Module Model
            implementation(projects.model)
            // Kotlin
            implementation(libs.kotlin.coroutine.core)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            // Room
            implementation(libs.androidx.room.runtime)
        }

        desktopMain.dependencies {
            // AndroidX Sqlite
            implementation(libs.androidx.sqlite.bundled)
        }

        named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

dependencies {
    // KSP & Room Compiler
    add("kspCommonMainMetadata", libs.koin.ksp)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspAndroid", libs.koin.ksp)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspDesktop", libs.koin.ksp)
}

tasks.matching {
    it.name.startsWith("ksp") &&
        it.name != "kspCommonMainKotlinMetadata"
}.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}
