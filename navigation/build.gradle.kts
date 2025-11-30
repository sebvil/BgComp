@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.composeLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.kotlinx.collections.immutable)

            implementation(project(":codegen-annotations"))
            implementation(project(":common"))
            implementation(project(":designsys"))
            implementation(project(":mvvm"))
            implementation(project(":resources"))
        }

        commonTest.dependencies {
            implementation(project(":testing"))
            implementation(project(":navigation-test"))
        }

        androidMain.dependencies { implementation(libs.androidx.activity.compose) }

        dependencies { implementation(libs.molecule.runtime) }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":mvvm-processor"))
    add("kspJvm", project(":mvvm-processor"))
    add("kspAndroid", project(":mvvm-processor"))
}
