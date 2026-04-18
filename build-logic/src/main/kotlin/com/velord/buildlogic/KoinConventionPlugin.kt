package com.velord.buildlogic

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("ksp")

        implementationPlatformLibrary("koin-bom")
        implementationBundle("koin")
        kspLibrary("koin-ksp")

        extensions.configure<KspExtension> {
            arg("KOIN_CONFIG_CHECK", "true")
            arg("KOIN_DEFAULT_MODULE", "false")
        }
    }
}
