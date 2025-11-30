plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.collections.immutable)

            implementation(project(":codegen-annotations"))
            implementation(project(":common"))
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":sealed-serializable-processor"))
    add("kspAndroid", project(":sealed-serializable-processor"))
    add("kspJvm", project(":sealed-serializable-processor"))
}
