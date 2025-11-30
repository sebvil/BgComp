plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.detekt)
}

group = "com.sebastianvm.convention"

version = "unspecified"

repositories {
    google()
    mavenCentral()
}

dependencies { implementation(libs.bundles.buildLogic) }

tasks.test { useJUnitPlatform() }

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "kmpLibrary"
            implementationClass = "com.sebastianvm.convention.MultiplatformLibraryPlugin"
        }

        register("composeLibrary") {
            id = "composeLibrary"
            implementationClass = "com.sebastianvm.convention.ComposeMultiplatformLibraryPlugin"
        }

        register("composeApplication") {
            id = "composeApplication"
            implementationClass = "com.sebastianvm.convention.ComposeMultiplatformApplicationPlugin"
        }

        register("featureLibrary") {
            id = "featureLibrary"
            implementationClass = "com.sebastianvm.convention.FeatureLibraryPlugin"
        }

        register("jvmApplication") {
            id = "jvmApplication"
            implementationClass = "com.sebastianvm.convention.JvmApplicationPlugin"
        }
    }
}

detekt {
    // Builds the AST in parallel. Rules are always executed in parallel.
    // Can lead to speedups in larger projects. `false` by default.
    parallel = false
    //
    //    // Define the detekt configuration(s) you want to use.
    //    // Defaults to the default detekt configuration.
    config.setFrom("$rootDir/../config/detekt/detekt.yml")

    // Applies the config files on top of detekt's default config file. `false` by default.
    buildUponDefaultConfig = false

    // Turns on all the rules. `false` by default.
    allRules = false

    // Adds debug output during task execution. `false` by default.
    debug = false

    // If set to `true` the build does not fail when there are any issues.
    // Defaults to `false`.
    ignoreFailures = false
}
