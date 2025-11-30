plugins { alias(libs.plugins.kmpLibrary) }

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.bundles.test)

            implementation(project(":mvvm"))
            implementation(project(":navigation"))
            implementation(project(":testing"))
        }
    }
}
