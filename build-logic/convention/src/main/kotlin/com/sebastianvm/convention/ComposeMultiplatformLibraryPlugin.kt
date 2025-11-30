package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply("androidLibrary")
            ComposeMultiplatformPlugin().apply(this)
        }
    }
}
