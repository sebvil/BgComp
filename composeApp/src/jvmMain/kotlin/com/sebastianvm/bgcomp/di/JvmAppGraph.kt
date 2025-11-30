package com.sebastianvm.bgcomp.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(scope = AppScope::class) interface JvmAppGraph : AppGraph
