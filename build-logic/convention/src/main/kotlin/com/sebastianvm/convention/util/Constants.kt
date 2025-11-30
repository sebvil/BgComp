package com.sebastianvm.convention.util

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun Project.getJvmTarget(): JvmTarget {
    val version = libs.version("jvmTarget")
    return JvmTarget.fromTarget(version)
}

fun Project.getJavaVersion(): JavaVersion {
    val version = libs.version("jvmTarget").toInt()
    return JavaVersion.toVersion(version)
}
