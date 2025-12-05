@file:Suppress("UnstableApiUsage")
rootProject.name = "bgcomp"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

include(":composeApp")

include(":server")

include(":designsys")

include("scripts")

include(":database")

include(":model")

include(":data")

include(":testing")

include(":codegen-annotations")

include(":mvvm-processor")

include(":serializers-processor")

include(":sealed-serializable-processor")

include(":mvvm")

include(":navigation")

include(":navigation-test")

include(":common")

include(":ui")

include(":feature-interfaces")

include(":features:home")

include(":features:kombio")

include(":resources")
