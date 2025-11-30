package com.sebastianvm.bgcomp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sebastianvm.bgcomp.di.JvmAppGraph
import com.sebastianvm.bgcomp.ui.JavaDateTimeFormatter
import com.sebastianvm.bgcomp.ui.LocalDateTimeFormatter
import dev.zacsweers.metro.createGraph

fun main() = application {
    val graph = createGraph<JvmAppGraph>()
    CompositionLocalProvider(LocalDateTimeFormatter provides JavaDateTimeFormatter()) {
        Window(onCloseRequest = ::exitApplication, title = "bgcomp") { graph.app() }
    }
}
