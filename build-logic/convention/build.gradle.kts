import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.velord.build_logic.convention"

java {
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.bundles.gradle)
    // Make version catalog available in precompiled scripts
    // https://github.com/gradle/gradle/issues/15383#issuecomment-1567461389
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

gradlePlugin {
    plugins {
        register("application") {
            id = "velord.application"
            implementationClass = "ApplicationConventionPlugin"
        }
    }
}
