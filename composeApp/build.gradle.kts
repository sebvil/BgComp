@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.composeApp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        androidMain.dependencies { implementation(libs.androidx.activity.compose) }
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.molecule.runtime)

            implementation(project(":designsys"))
            implementation(project(":model"))
            implementation(project(":mvvm"))
            implementation(project(":data"))
            implementation(project(":navigation"))
            implementation(project(":codegen-annotations"))
            implementation(project(":common"))
            implementation(project(":ui"))
            implementation(project(":feature-interfaces"))
            implementation(project(":resources"))

            implementation(project(":features:home"))
            implementation(project(":features:kombio"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.sebastianvm.bgcomp"

    defaultConfig {
        applicationId = "com.sebastianvm.bgcomp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes { getByName("release") { isMinifyEnabled = false } }
}

compose.desktop {
    application {
        mainClass = "com.sebastianvm.bgcomp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.sebastianvm.bgcomp"
            packageVersion = "1.0.0"
        }
    }
}
