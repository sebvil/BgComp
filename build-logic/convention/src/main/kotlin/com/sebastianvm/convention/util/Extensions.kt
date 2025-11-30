package com.sebastianvm.convention.util

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.library(lib: String) = findLibrary(lib).get()

fun VersionCatalog.bundle(bundle: String): Provider<ExternalModuleDependencyBundle> =
    findBundle(bundle).get()

fun VersionCatalog.plugin(plugin: String): String = findPlugin(plugin).get().get().pluginId

fun VersionCatalog.version(version: String): String = findVersion(version).get().requiredVersion

fun Project.plugin(plugin: String) = libs.plugin(plugin)

fun Project.apply(plugin: String) {
    plugins.apply(plugin(plugin))
}
