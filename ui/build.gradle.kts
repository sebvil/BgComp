plugins { alias(libs.plugins.composeLibrary) }

kotlin {
    applyDefaultHierarchyTemplate()
    sourceSets {
        val androidAndJvmMain by creating { dependsOn(commonMain.get()) }

        androidMain { dependsOn(androidAndJvmMain) }
        jvmMain { dependsOn(androidAndJvmMain) }
        commonMain.dependencies { implementation(libs.kotlinx.datetime) }
    }
}
