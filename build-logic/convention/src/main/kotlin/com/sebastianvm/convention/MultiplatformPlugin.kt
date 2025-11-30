package com.sebastianvm.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.sebastianvm.convention.util.apply
import com.sebastianvm.convention.util.bundle
import com.sebastianvm.convention.util.getJavaVersion
import com.sebastianvm.convention.util.getJvmTarget
import com.sebastianvm.convention.util.libs
import com.sebastianvm.convention.util.version
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "kotlinMultiplatform")
            apply(plugin = "metro")
            apply(plugin = "testBalloon")

            DetektPlugin().apply(this)

            configure<KotlinMultiplatformExtension> {
                androidTarget { compilerOptions { jvmTarget.set(getJvmTarget()) } }
                jvm()

                sourceSets.apply {
                    commonTest.dependencies {
                        commonTest.dependencies { implementation(libs.bundle("test")) }
                    }
                }

                compilerOptions {
                    freeCompilerArgs.add("-Xcontext-parameters")
                    optIn.add("kotlin.uuid.ExperimentalUuidApi")
                }
            }

            configure<MetroPluginExtension> { contributesAsInject.set(true) }

            configure<BaseExtension> {
                when (this) {
                    is LibraryExtension -> {
                        compileSdk = libs.version("android.compileSdk").toInt()
                        namespace =
                            "com.sebastianvm.${project.group}.${project.name}"
                                .replace("-[a-z]".toRegex()) {
                                    it.value.replace("-", "").uppercase()
                                }
                    }

                    is ApplicationExtension -> {
                        compileSdk = libs.version("android.compileSdk").toInt()
                    }
                }
                defaultConfig { minSdk = libs.version("android.minSdk").toInt() }
                compileOptions {
                    sourceCompatibility = getJavaVersion()
                    targetCompatibility = getJavaVersion()
                }
            }
        }
    }
}
