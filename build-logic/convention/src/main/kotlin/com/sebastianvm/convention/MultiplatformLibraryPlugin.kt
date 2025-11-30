package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

class MultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply("androidLibrary")
            MultiplatformPlugin().apply(this)
        }
    }
}
