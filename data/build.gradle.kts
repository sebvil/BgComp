plugins { alias(libs.plugins.kmpLibrary) }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            api(project(":database"))
            implementation(project(":model"))
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.collections.immutable)
        }
    }
}
