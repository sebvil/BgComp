@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm()

    sourceSets {
        androidMain.dependencies { implementation(libs.sqldelight.android.driver) }
        commonMain.dependencies {
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.datetime)

            implementation(project(":model"))
        }

        jvmMain.dependencies { implementation(libs.sqldelight.sqlite.driver) }
    }
}

sqldelight {
    databases { create("BgCompDatabase") { packageName.set("com.sebastianvm.bgcomp.database") } }
}
