package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import com.sebastianvm.convention.util.getJavaVersion
import com.sebastianvm.convention.util.getJvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class JvmApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "kotlinJvm")

            DetektPlugin().apply(this)

            configure<KotlinJvmProjectExtension> {
                compilerOptions {
                    jvmTarget.set(getJvmTarget())
                    freeCompilerArgs.add("-Xcontext-parameters")
                    optIn.add("kotlin.uuid.ExperimentalUuidApi")
                }
            }

            configure<JavaPluginExtension> {
                sourceCompatibility = getJavaVersion()
                targetCompatibility = getJavaVersion()
            }
        }
    }
}
