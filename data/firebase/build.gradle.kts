plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.data.firebase"
    }

    sourceSets {
        commonMain.dependencies {
            // Firebase
            implementation(gitliveLibs.firebase.config)
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
        }
        androidMain.dependencies {
            // GitLive keeps Firebase Android SDK versions BOM-managed.
            implementation(project.dependencies.platform(libs.google.firebase.bom))
        }
        desktopMain.dependencies {
            // GitLive's JVM artifact uses the same Firebase dependency constraints.
            implementation(project.dependencies.platform(libs.google.firebase.bom))
        }
    }

    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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
