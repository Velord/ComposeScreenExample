plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

dependencies {
    // Testing
    testImplementation(libs.konsist)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    inputs.files(
        rootProject.fileTree(rootProject.projectDir) {
            include("**/src/**/*.kt")
            include("**/*.gradle.kts")
            include("gradle/libs.versions.toml")
            exclude("**/build/**")
            exclude("**/.gradle/**")
        }
    ).withPathSensitivity(PathSensitivity.RELATIVE)

    useJUnitPlatform()
}
