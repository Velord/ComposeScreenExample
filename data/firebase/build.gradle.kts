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
            // Koin
            implementation(libs.koin.core)
            api(libs.koin.annotation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutine.test)
        }
        androidMain.dependencies {
            // Firebase Remote Config
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation("com.google.firebase:firebase-config")
            implementation(libs.kotlin.coroutine.play.service)
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
