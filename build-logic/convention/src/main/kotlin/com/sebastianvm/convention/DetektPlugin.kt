package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import com.sebastianvm.convention.util.library
import com.sebastianvm.convention.util.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply("detekt")

            extensions.configure<DetektExtension> {
                // Builds the AST in parallel. Rules are always executed in parallel.
                // Can lead to speedups in larger projects. `false` by default.
                parallel = false

                // Define the detekt configuration(s) you want to use.
                // Defaults to the default detekt configuration.
                config.setFrom(
                    "$rootDir/config/detekt/detekt.yml",
                    "$rootDir/config/detekt/compose.yml",
                )
                // Applies the config files on top of detekt's default config file. `false` by
                // default.
                buildUponDefaultConfig = false

                // Turns on all the rules. `false` by default.
                allRules = false

                // Adds debug output during task execution. `false` by default.
                debug = false

                // If set to `true` the build does not fail when there are any issues.
                // Defaults to `false`.
                ignoreFailures = false

                // Android: Don't create tasks for the specified build types (e.g. "release")
                ignoredBuildTypes = listOf("release")

                // Android: Don't create tasks for the specified build flavor (e.g. "production")
                ignoredFlavors = listOf("production")

                // Android: Don't create tasks for the specified build variants (e.g.
                // "productionRelease")
                ignoredVariants = listOf("productionRelease")

                // Load baseline file if it exists
                val baselineFile = file("detekt-baseline.xml")
                if (baselineFile.exists()) {
                    baseline = baselineFile
                }
            }

            tasks.withType<Detekt>().configureEach {
                val projectDir = projectDir
                val buildDir = project.layout.buildDirectory.asFile.get()

                exclude {
                    it.file.relativeTo(projectDir).startsWith(buildDir.relativeTo(projectDir))
                }
            }

            dependencies { add("detektPlugins", libs.library("compose.rules.detekt")) }
        }
    }
}
