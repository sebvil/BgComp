plugins {
    id("jvmApplication")
    application
}

group = "com.sebastianvm.scripts"

version = "unspecified"

repositories { mavenCentral() }

dependencies {
    implementation(libs.clikt)
    testImplementation(kotlin("test"))
}

application { mainClass.set("com.sebastianvm.scripts.MainKt") }
