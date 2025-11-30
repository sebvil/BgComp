plugins { alias(libs.plugins.kmpLibrary) }

kotlin {
    sourceSets { commonMain.dependencies { implementation(libs.kotlinx.serialization.core) } }
}
