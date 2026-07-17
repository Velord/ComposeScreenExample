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
            implementationClass = "com.velord.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibraryConvention") {
            id = "velord.android.library"
            implementationClass = "com.velord.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidComposeConvention") {
            id = "velord.android.compose"
            implementationClass = "com.velord.buildlogic.AndroidComposeConventionPlugin"
        }
        register("androidViewBindingConvention") {
            id = "velord.android.viewbinding"
            implementationClass = "com.velord.buildlogic.AndroidViewBindingConventionPlugin"
        }
        register("koinConvention") {
            id = "velord.koin"
            implementationClass = "com.velord.buildlogic.KoinConventionPlugin"
        }
        register("featureUiConvention") {
            id = "velord.feature.ui"
            implementationClass = "com.velord.buildlogic.FeatureUiConventionPlugin"
        }
        register("featureUiKoinConvention") {
            id = "velord.feature.ui.koin"
            implementationClass = "com.velord.buildlogic.FeatureUiKoinConventionPlugin"
        }
        register("widgetGlanceConvention") {
            id = "velord.widget.glance"
            implementationClass = "com.velord.buildlogic.WidgetGlanceConventionPlugin"
        }
        register("kmpLibraryConvention") {
            id = "velord.kmp.library"
            implementationClass = "com.velord.buildlogic.KmpLibraryConventionPlugin"
        }
        register("domainUsecaseKmpConvention") {
            id = "velord.domain.usecase.kmp"
            implementationClass = "com.velord.buildlogic.DomainUsecaseKmpConventionPlugin"
        }
        register("buildConfigConvention") {
            id = "velord.buildconfig"
            implementationClass = "com.velord.buildlogic.BuildConfigConventionPlugin"
        }
    }
}

// TODO: tech debt. Names are not from toml.
dependencies {
    val buildKonfigVersion = libs.versions.buildkonfig.get()
    val googleKspPlugin = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:" +
        libs.versions.googleKsp.get()

    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.compose)
    implementation("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:$buildKonfigVersion")
    implementation(googleKspPlugin)
}
