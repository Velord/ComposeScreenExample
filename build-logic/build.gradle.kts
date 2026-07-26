plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
}

gradlePlugin {
    plugins {
        register("androidApplicationConvention") {
            id = "velord.android.application"
            implementationClass = "com.velord.buildlogic.plugin.android" +
                    ".AndroidApplicationConventionPlugin"
        }
        register("androidLibraryConvention") {
            id = "velord.android.library"
            implementationClass = "com.velord.buildlogic.plugin.android" +
                    ".AndroidLibraryConventionPlugin"
        }
        register("androidComposeConvention") {
            id = "velord.android.compose"
            implementationClass = "com.velord.buildlogic.plugin.android" +
                    ".AndroidComposeConventionPlugin"
        }
        register("androidViewBindingConvention") {
            id = "velord.android.viewbinding"
            implementationClass = "com.velord.buildlogic.plugin.android" +
                    ".AndroidViewBindingConventionPlugin"
        }
        register("koinConvention") {
            id = "velord.koin"
            implementationClass = "com.velord.buildlogic.plugin.KoinConventionPlugin"
        }
        register("featureUiConvention") {
            id = "velord.feature.ui"
            implementationClass = "com.velord.buildlogic.plugin.module.FeatureUiConventionPlugin"
        }
        register("featureUiKoinConvention") {
            id = "velord.feature.ui.koin"
            implementationClass =
                "com.velord.buildlogic.plugin.module.FeatureUiKoinConventionPlugin"
        }
        register("widgetGlanceConvention") {
            id = "velord.widget.glance"
            implementationClass =
                "com.velord.buildlogic.plugin.module.WidgetGlanceConventionPlugin"
        }
        register("kmpLibraryConvention") {
            id = "velord.kmp.library"
            implementationClass = "com.velord.buildlogic.plugin.KmpLibraryConventionPlugin"
        }
        register("domainUsecaseKmpConvention") {
            id = "velord.domain.usecase"
            implementationClass = "com.velord.buildlogic.plugin.module" +
                    ".DomainUsecaseKmpConventionPlugin"
        }
        register("buildConfigConvention") {
            id = "velord.buildconfig"
            implementationClass = "com.velord.buildlogic.plugin.BuildConfigConventionPlugin"
        }
    }
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.compose)
    implementation(libs.gradle.buildkonfig)
    implementation(libs.gradle.ksp)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
}
