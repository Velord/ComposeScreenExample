plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.velord.model"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.datetime)
            api(libs.kamera.core)
            implementation(libs.kotlin.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
