package com.sebastianvm.bgcomp.testing.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.MvvmComponent
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.mvvm.Props
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.mvvm.UiState
import com.sebastianvm.bgcomp.mvvm.UserAction
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.flow.StateFlow

interface TestAppGraph {

    val testScope: CloseableCoroutineScope

    @Provides
    val recompositionMode: RecompositionMode
        get() = RecompositionMode.Immediate

    @Provides
    val mvvmComponentInitializer: MvvmComponentInitializerProvider
        get() =
            object : MvvmComponentInitializerProvider {
                override fun <P : Props> invoke(): MvvmComponentInitializer<P> {
                    return object : MvvmComponentInitializer<P> {
                        override fun initialize(
                            args: MvvmComponentArguments,
                            props: StateFlow<P>,
                        ): MvvmComponent<*, P, *, *, *> = FakeMvvmComponent(args, props)
                    }
                }
            }

    interface Factory<G : TestAppGraph> {
        fun create(testScope: CloseableCoroutineScope): G
    }
}

interface MvvmComponentInitializerProvider {
    operator fun <P : Props> invoke(): MvvmComponentInitializer<P>
}

data class FakeMvvmComponent<P : Props>(
    override val arguments: MvvmComponentArguments,
    val props: StateFlow<P>,
) : MvvmComponent<MvvmComponentArguments, P, UiState, UserAction, Ui<UiState, UserAction>> {
    @Composable override fun Content(modifier: Modifier) = Unit
}
