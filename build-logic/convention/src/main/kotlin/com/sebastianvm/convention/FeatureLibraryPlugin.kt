package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import com.sebastianvm.convention.util.library
import com.sebastianvm.convention.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            ComposeMultiplatformLibraryPlugin().apply(this)
            apply("ksp")

            // Configure common feature dependencies
            configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        // Kotlinx libraries
                        implementation(libs.library("kotlinx.coroutines"))

                        // Compose & lifecycle libraries
                        implementation(libs.library("molecule.runtime"))
                        implementation(libs.library("androidx.lifecycle.viewmodelCompose"))
                        implementation(libs.library("androidx.navigation3.runtime"))

                        implementation(project(":designsys"))
                        implementation(project(":model"))
                        implementation(project(":mvvm"))
                        implementation(project(":data"))
                        implementation(project(":navigation"))
                        implementation(project(":mvvm"))
                        implementation(project(":codegen-annotations"))
                        implementation(project(":common"))
                        implementation(project(":ui"))
                        implementation(project(":feature-interfaces"))
                        implementation(project(":resources"))
                    }

                    commonTest.dependencies {
                        implementation(project(":testing"))
                        implementation(project(":navigation-test"))
                    }
                }
            }

            // Configure KSP dependencies
            dependencies {
                // Add debugImplementation for compose.uiTooling

                // Add KSP processor dependencies if mvvm-processor exists
                rootProject.findProject(":mvvm-processor")?.let { processorProject ->
                    add("kspCommonMainMetadata", processorProject)
                    add("kspJvm", processorProject)
                    add("kspAndroid", processorProject)
                }
            }
        }
    }
}
