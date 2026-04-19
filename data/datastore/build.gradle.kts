plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.datastore"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.koin.core)
            api(libs.koin.annotation)
            implementation(libs.androidx.datastore.core.okio)
            implementation(libs.kotlin.serialization.json)
        }

        named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
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
