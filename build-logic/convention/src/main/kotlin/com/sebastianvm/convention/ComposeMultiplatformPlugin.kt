package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import com.sebastianvm.convention.util.library
import com.sebastianvm.convention.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            MultiplatformPlugin().apply(this)
            apply("composeMultiplatform")
            apply("composeCompiler")

            val composeExtension = extensions.getByType<ComposeExtension>()

            configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(libs.library("compose.ui.tooling.preview"))
                    }

                    commonMain.dependencies {
                        implementation(libs.library("compose.runtime"))
                        implementation(libs.library("compose.foundation"))
                        implementation(libs.library("compose.ui"))
                        implementation(libs.library("compose.components.resources"))
                        implementation(libs.library("compose.ui.tooling.preview"))
                    }
                }
            }

            composeExtension.configure<ResourcesExtension> {
                generateResClass = ResourcesExtension.ResourceClassGeneration.Never
            }

            dependencies { add("debugImplementation", libs.library("compose.ui.tooling")) }

            configure<ComposeCompilerGradlePluginExtension> {
                this.stabilityConfigurationFiles.add { file("$rootDir/compose_stability.conf") }
            }
        }
    }
}
