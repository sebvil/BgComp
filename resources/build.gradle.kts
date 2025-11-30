plugins { alias(libs.plugins.composeLibrary) }

compose.resources {
    publicResClass = true
    packageOfResClass = "com.sebastianvm.bgcomp.resources"
    generateResClass = auto
}
