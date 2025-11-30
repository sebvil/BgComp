package com.sebastianvm.scripts

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.io.File

class GenerateModuleCommand : CliktCommand(name = "generate-module") {

    override fun help(context: Context): String = "Generate a new KMP module"

    private val moduleName by option("--name", "-n", help = "Module name").required()

    private val packageName: String
        get() {
            val suffix = moduleName.replace("-", "").lowercase()
            return "com.sebastianvm.bgcomp.$suffix"
        }

    private val isApplication by
        option("--application", "-a", help = "Create an application module instead of library")
            .flag(default = false)
    private val includeAndroid by
        option("--android", help = "Include Android target").flag(default = true)
    private val includeJvm by option("--jvm", help = "Include JVM target").flag(default = true)
    private val includeJs by option("--js", help = "Include JS target").flag(default = false)
    private val includeWasm by option("--wasm", help = "Include Wasm target").flag(default = false)

    private fun getRepositoryRoot(): File {
        val process =
            ProcessBuilder("git", "rev-parse", "--show-toplevel")
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        val output = process.inputStream.bufferedReader().readText().trim()
        val exitCode = process.waitFor()

        if (exitCode != 0) {
            echo("Error: Not in a git repository", err = true)
            error("Failed to get repository root")
        }

        return File(output)
    }

    override fun run() {
        val rootDir = getRepositoryRoot()
        val moduleDir = File(rootDir, moduleName)

        if (moduleDir.exists()) {
            echo("Error: Module directory '$moduleName' already exists", err = true)
            return
        }

        echo("Creating KMP module: $moduleName")
        echo("Package: $packageName")
        echo("Type: ${if (isApplication) "Application" else "Library"}")
        echo("Targets: ${getTargets()}")

        createModuleStructure(moduleDir)
        generateBuildGradleKts(moduleDir)
        updateSettingsGradle(rootDir)

        echo("Module '$moduleName' created successfully!")
        echo("Next steps:")
        echo("  1. Sync Gradle project")
        echo("  2. Add your code to $moduleName/src/commonMain/kotlin")
        echo("  3. Add dependencies in $moduleName/build.gradle.kts")
    }

    private fun getTargets(): String {
        val targets = mutableListOf<String>()
        if (includeAndroid) targets.add("Android")
        if (includeJvm) targets.add("JVM")
        if (includeJs) targets.add("JS")
        if (includeWasm) targets.add("Wasm")
        return targets.joinToString(", ")
    }

    private fun createModuleStructure(moduleDir: File) {
        echo("Creating directory structure...")

        // Create source sets
        val sourceSets = buildList {
            add("src/commonMain/kotlin/${packageName.replace('.', '/')}")
            add("src/commonTest/kotlin/${packageName.replace('.', '/')}")

            if (includeAndroid) {
                add("src/androidMain/kotlin/${packageName.replace('.', '/')}")
                add("src/androidInstrumentedTest/kotlin/${packageName.replace('.', '/')}")
            }

            if (includeJvm) {
                add("src/jvmMain/kotlin/${packageName.replace('.', '/')}")
                add("src/jvmTest/kotlin/${packageName.replace('.', '/')}")
            }

            if (includeJs) {
                add("src/jsMain/kotlin/${packageName.replace('.', '/')}")
                add("src/jsTest/kotlin/${packageName.replace('.', '/')}")
            }

            if (includeWasm) {
                add("src/wasmJsMain/kotlin/${packageName.replace('.', '/')}")
                add("src/wasmJsTest/kotlin/${packageName.replace('.', '/')}")
            }
        }

        sourceSets.forEach { sourceSet ->
            val dir = File(moduleDir, sourceSet)
            dir.mkdirs()
        }

        // Create a sample Greeting class
        val greetingFile =
            File(moduleDir, "src/commonMain/kotlin/${packageName.replace('.', '/')}/Greeting.kt")
        greetingFile.parentFile.mkdirs()
        greetingFile.writeText(
            $$"""
            package $$packageName

            class Greeting {
                private val platform = getPlatform()

                fun greet(): String {
                    return "Hello, ${platform.name}!"
                }
            }

        """
                .trimIndent()
        )

        // Create Platform interface
        val platformFile =
            File(moduleDir, "src/commonMain/kotlin/${packageName.replace('.', '/')}/Platform.kt")
        platformFile.parentFile.mkdirs()
        platformFile.writeText(
            """
                package $packageName
                    
                interface Platform {
                    val name: String
                }
                
                expect fun getPlatform(): Platform
                
            """
                .trimIndent()
        )

        // Create Android platform implementation
        if (includeAndroid) {
            val androidPlatformFile =
                File(
                    moduleDir,
                    "src/androidMain/kotlin/${packageName.replace('.', '/')}/Platform.android.kt",
                )
            androidPlatformFile.parentFile.mkdirs()
            androidPlatformFile.writeText(
                $$"""
                package $$packageName

                class AndroidPlatform : Platform {
                    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
                }

                actual fun getPlatform(): Platform = AndroidPlatform()

            """
                    .trimIndent()
            )
        }

        // Create JVM platform implementation
        if (includeJvm) {
            val jvmPlatformFile =
                File(
                    moduleDir,
                    "src/jvmMain/kotlin/${packageName.replace('.', '/')}/Platform.jvm.kt",
                )
            jvmPlatformFile.parentFile.mkdirs()
            jvmPlatformFile.writeText(
                $$"""
                package $$packageName

                class JVMPlatform : Platform {
                    override val name: String = "Java ${System.getProperty("java.version")}"
                }

                actual fun getPlatform(): Platform = JVMPlatform()

            """
                    .trimIndent()
            )
        }

        // Create JS platform implementation
        if (includeJs) {
            val jsPlatformFile =
                File(moduleDir, "src/jsMain/kotlin/${packageName.replace('.', '/')}/Platform.js.kt")
            jsPlatformFile.parentFile.mkdirs()
            jsPlatformFile.writeText(
                """
                    package $packageName
                    
                    class JSPlatform : Platform {
                        override val name: String = "JavaScript"
                    }
                    
                    actual fun getPlatform(): Platform = JSPlatform()
                    
                """
                    .trimIndent()
            )
        }

        // Create Wasm platform implementation
        if (includeWasm) {
            val wasmPlatformFile =
                File(
                    moduleDir,
                    "src/wasmJsMain/kotlin/${packageName.replace('.', '/')}/Platform.wasmJs.kt",
                )
            wasmPlatformFile.parentFile.mkdirs()
            wasmPlatformFile.writeText(
                """
                    package $packageName
                    
                    class WasmPlatform : Platform {
                        override val name: String = "WebAssembly"
                    }
                    
                    actual fun getPlatform(): Platform = WasmPlatform()

                """
                    .trimIndent()
            )
        }
    }

    private fun generateBuildGradleKts(moduleDir: File) {
        echo("Generating build.gradle.kts...")

        val buildFile = File(moduleDir, "build.gradle.kts")
        val content = buildString {
            // Imports
            if (includeWasm) {
                appendLine("import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl")
            }
            if (includeAndroid || includeJvm) {
                appendLine("import org.jetbrains.kotlin.gradle.dsl.JvmTarget")
            }
            appendLine()

            // Plugins
            appendLine("plugins {")
            appendLine("    alias(libs.plugins.kotlinMultiplatform)")
            if (isApplication) {
                appendLine("    alias(libs.plugins.androidApplication)")
            } else if (includeAndroid) {
                appendLine("    alias(libs.plugins.androidLibrary)")
            }
            appendLine("    alias(libs.plugins.metro)")
            appendLine("}")
            appendLine()

            // Kotlin block
            appendLine("kotlin {")

            // Targets
            if (includeAndroid) {
                appendLine("    androidTarget {")
                appendLine("        compilerOptions {")
                appendLine("            jvmTarget.set(JvmTarget.JVM_11)")
                appendLine("        }")
                appendLine("    }")
                appendLine()
            }

            if (includeJvm) {
                appendLine("    jvm()")
                appendLine()
            }

            if (includeJs) {
                appendLine("    js {")
                appendLine("        browser()")
                appendLine("    }")
                appendLine()
            }

            if (includeWasm) {
                appendLine("    @OptIn(ExperimentalWasmDsl::class)")
                appendLine("    wasmJs {")
                appendLine("        browser()")
                appendLine("    }")
                appendLine()
            }

            // Source sets
            appendLine("    sourceSets {")
            appendLine("        commonMain.dependencies {")
            appendLine("            // Add your common dependencies here")
            appendLine("        }")
            appendLine("        commonTest.dependencies {")
            appendLine("            implementation(libs.bundles.test)")
            appendLine("        }")
            appendLine("    }")
            appendLine("}")
            appendLine()

            // Metro block
            appendLine("metro {")
            appendLine("    contributesAsInject.set(true)")
            appendLine("}")

            // Android block
            if (includeAndroid) {
                appendLine()
                appendLine("android {")
                appendLine("    namespace = \"$packageName\"")
                appendLine("    compileSdk = libs.versions.android.compileSdk.get().toInt()")

                if (isApplication) {
                    appendLine()
                    appendLine("    defaultConfig {")
                    appendLine("        applicationId = \"$packageName\"")
                    appendLine("        minSdk = libs.versions.android.minSdk.get().toInt()")
                    appendLine("        targetSdk = libs.versions.android.targetSdk.get().toInt()")
                    appendLine("        versionCode = 1")
                    appendLine("        versionName = \"1.0\"")
                    appendLine("    }")
                } else {
                    appendLine("    defaultConfig {")
                    appendLine("        minSdk = libs.versions.android.minSdk.get().toInt()")
                    appendLine("    }")
                }

                appendLine("    compileOptions {")
                appendLine("        sourceCompatibility = JavaVersion.VERSION_11")
                appendLine("        targetCompatibility = JavaVersion.VERSION_11")
                appendLine("    }")
                appendLine("}")
            }
        }

        buildFile.parentFile.mkdirs()
        buildFile.writeText(content)
    }

    private fun updateSettingsGradle(rootDir: File) {
        echo("Updating settings.gradle.kts...")

        val settingsFile = File(rootDir, "settings.gradle.kts")
        if (!settingsFile.exists()) {
            echo("Warning: settings.gradle.kts not found", err = true)
            return
        }

        val content = settingsFile.readText()
        val includeStatement =
            """
                include(":$moduleName")
            """
                .trimIndent()

        if (content.contains(includeStatement.trim())) {
            echo("Module already included in settings.gradle.kts")
            return
        }

        settingsFile.appendText(includeStatement)
    }
}
