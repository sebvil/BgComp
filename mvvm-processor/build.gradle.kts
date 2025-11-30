plugins { kotlin("jvm") }

dependencies {
    implementation(project(":codegen-annotations"))
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}
