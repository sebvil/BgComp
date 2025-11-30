@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.composeLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.kotlinx.collections.immutable)

            implementation(project(":designsys"))
            implementation(project(":resources"))
        }

        androidMain.dependencies { implementation(libs.androidx.activity.compose) }

        jvmMain.dependencies { implementation(libs.kotlinx.coroutinesSwing) }

        dependencies { implementation(libs.molecule.runtime) }
    }
}
