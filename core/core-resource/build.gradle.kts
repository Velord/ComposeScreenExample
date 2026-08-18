plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.velord.core.resource"
}

kotlin {
    android {
        namespace = "com.velord.core.resource"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.resources)
        }
    }
}
