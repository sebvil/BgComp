package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply("androidApplication")
            ComposeMultiplatformPlugin().apply(this)
        }
    }
}
