plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.os"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.infrastructure.config)
            implementation(projects.model)
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kamera.core)
            implementation(libs.koin.core)
            implementation(libs.kermit)
            api(libs.koin.annotation)
        }

        androidMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.kamera.core)
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.crashlytic)
        }

        desktopMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            implementation(libs.kamera.core)
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
