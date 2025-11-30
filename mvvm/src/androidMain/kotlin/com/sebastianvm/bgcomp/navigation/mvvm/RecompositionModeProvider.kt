package com.sebastianvm.bgcomp.navigation.mvvm

import app.cash.molecule.RecompositionMode
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface RecompositionModeProvider {

    @Provides fun provideRecompositionMode(): RecompositionMode = RecompositionMode.ContextClock
}
