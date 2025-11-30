package com.sebastianvm.bgcomp.di

import com.sebastianvm.bgcomp.App
import dev.zacsweers.metro.Provides
import kotlinx.serialization.modules.SerializersModule

interface AppGraph {
    val app: App

    @Provides
    fun serializersModule(modules: Set<SerializersModule>): SerializersModule = SerializersModule {
        modules.forEach { include(it) }
    }
}
