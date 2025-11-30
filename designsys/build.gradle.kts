plugins { alias(libs.plugins.composeLibrary) }

kotlin { sourceSets { commonMain.dependencies { implementation(libs.compose.material3) } } }
