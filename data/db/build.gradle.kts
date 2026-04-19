plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.db"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            implementation(libs.androidx.room.runtime)
        }

        named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

dependencies {
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
