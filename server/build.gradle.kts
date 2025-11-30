plugins {
    id("jvmApplication")
    alias(libs.plugins.ktor)
    application
}

group = "com.sebastianvm.bgcomp"

version = "1.0.0"

application {
    mainClass.set("com.sebastianvm.bgcomp.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.logback)
    implementation(project(":common"))
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.bundles.test)
}
