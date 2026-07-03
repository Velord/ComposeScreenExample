plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

dependencies {
    testImplementation(libs.konsist)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
