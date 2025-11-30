plugins { alias(libs.plugins.kmpLibrary) }

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            // Add your common dependencies here
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.molecule.runtime)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.bundles.test)

            implementation(project(":data"))
            implementation(project(":model"))
            implementation(project(":mvvm"))
            implementation(project(":common"))
        }
    }
}
