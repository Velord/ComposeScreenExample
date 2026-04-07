package com.velord.buildlogic

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        val versionCatalog = libs
        val koinBom = versionCatalog.findLibrary("koin-bom").get().get()
        val koinBundle = versionCatalog.findBundle("koin").get().get()
        val koinKsp = versionCatalog.findLibrary("koin-ksp").get().get()

        dependencies {
            add("implementation", platform(koinBom))
            koinBundle.forEach { dependency ->
                add("implementation", dependency)
            }
            add("ksp", koinKsp)
        }

        extensions.configure<KspExtension> {
            arg("KOIN_CONFIG_CHECK", "true")
            arg("KOIN_DEFAULT_MODULE", "false")
        }
    }
}
