import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import dev.detekt.gradle.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dagger.hilt) apply false

    // Add Firebase plugins here
    alias(libs.plugins.google.gms.services) apply false
    alias(libs.plugins.google.firebase.crashlytic) apply false
}

extensions.configure<DetektExtension> {
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
    baseline.set(rootProject.file("config/detekt/baseline.xml"))
    buildUponDefaultConfig = true
    parallel = true
    ignoreFailures = false
    basePath.set(rootProject.projectDir)
}

subprojects {
    fun configureDetekt() {
        pluginManager.apply("dev.detekt")

        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("config/detekt/detekt.yml"))
            baseline.set(rootProject.file("config/detekt/baseline.xml"))
            buildUponDefaultConfig = true
            parallel = true
            ignoreFailures = false
            basePath.set(rootProject.projectDir)
        }

        tasks.withType(Detekt::class.java).configureEach {
            jvmTarget.set("21")
            exclude("**/build/**")

            reports {
                checkstyle.required.set(true)
                html.required.set(true)
                sarif.required.set(true)
                markdown.required.set(true)
            }
        }

        tasks.withType(DetektCreateBaselineTask::class.java).configureEach {
            jvmTarget.set("21")
            exclude("**/build/**")
            enabled = false
        }

    }

    pluginManager.withPlugin("com.android.application") {
        configureDetekt()
    }

    pluginManager.withPlugin("com.android.library") {
        configureDetekt()
    }
}

val detektProjectBaseline by tasks.registering(DetektCreateBaselineTask::class) {
    description = "Creates a shared detekt baseline for all Gradle modules."
    buildUponDefaultConfig.set(true)
    debug.set(false)
    ignoreFailures.set(true)
    multiPlatformEnabled.set(false)
    noJdk.set(false)
    parallel.set(true)
    jvmTarget.set("21")
    setSource(files(subprojects.map { it.projectDir }))
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline.set(file("$rootDir/config/detekt/baseline.xml"))
    include("**/*.kt")
    exclude("**/resources/**")
    exclude("**/build/**")
    exclude("**/.gradle/**")
}

// Fast way to clean project
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
