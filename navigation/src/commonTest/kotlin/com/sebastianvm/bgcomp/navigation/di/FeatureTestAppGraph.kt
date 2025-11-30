package com.sebastianvm.bgcomp.navigation.di

import com.sebastianvm.bgcomp.common.di.TestScope
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.testing.di.TestAppGraph
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(TestScope::class)
interface FeatureTestAppGraph : TestAppGraph {

    @DependencyGraph.Factory
    interface Factory : TestAppGraph.Factory<FeatureTestAppGraph> {
        override fun create(@Provides testScope: CloseableCoroutineScope): FeatureTestAppGraph
    }
}
