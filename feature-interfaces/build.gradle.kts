plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.androidx.navigation3.runtime)

            implementation(project(":mvvm"))
            implementation(project(":model"))
            implementation(project(":navigation"))
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":serializers-processor"))
    add("kspAndroid", project(":serializers-processor"))
    add("kspJvm", project(":serializers-processor"))
}
