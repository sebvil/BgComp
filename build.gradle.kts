import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.KtfmtStep

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.testBalloon) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
}

// Configure Spotless for the root project (this file, settings, etc.)
spotless {
    kotlinGradle {
        target("**/*.gradle.kts")
        setupKtfmt()
    }
    format("misc-root") {
        target("**/.gitignore", "**/*.md", "**/*.yaml", "**/*.yml")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlin {
        // Format all Kotlin source files
        target("**/*.kt")
        // Exclude generated code if any
        targetExclude("**/build/**")
        // Use ktfmt as the formatter
        setupKtfmt()
    }
}

fun BaseKotlinExtension.setupKtfmt() {
    ktfmt().kotlinlangStyle().configure {
        it.setRemoveUnusedImports(true)
        it.setTrailingCommaManagementStrategy(KtfmtStep.TrailingCommaManagementStrategy.COMPLETE)
        it.setBlockIndent(4)
    }
}

fun getDetektTasks() =
    subprojects.flatMap { subproject ->
        subproject.tasks.filter {
            it.name in listOf("detektMetadataMain", "detektMain", "detektTest")
        }
    }

// Create a linters task that runs all linting checks
tasks.register("linters") {
    description = "Run all linting tasks"
    group = "verification"

    val detektTasks = getDetektTasks()
    val spotlessTasks = tasks.matching { it.name == "spotlessApply" }

    dependsOn(
        spotlessTasks,
        gradle.includedBuild("build-logic").task(":convention:detektMain"),
        gradle.includedBuild("build-logic").task(":convention:detektTest"),
        detektTasks,
    )
    detektTasks.forEach { it.mustRunAfter(spotlessTasks) }
}
