plugins {
    alias(libs.plugins.featureLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.savedstate.ktx)
        }

        commonTest.dependencies { implementation(libs.kotlin.test) }
    }
}

android { testOptions { this.unitTests.isReturnDefaultValues = true } }
