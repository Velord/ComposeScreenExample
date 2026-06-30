plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.os"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(libs.koin.annotation)
        }

        androidMain.dependencies {
            implementation(projects.model)
            implementation(projects.core.coreResource)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            implementation(libs.androidx.camera.video)
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

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
