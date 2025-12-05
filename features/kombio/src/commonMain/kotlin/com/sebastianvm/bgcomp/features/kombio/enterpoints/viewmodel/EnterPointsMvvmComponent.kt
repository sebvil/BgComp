package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sebastianvm.bgcomp.featureinterfaces.EnterPointsArguments
import com.sebastianvm.bgcomp.features.kombio.enterpoints.ui.EnterPointsUi
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.mvvm.MvvmComponent
import com.sebastianvm.bgcomp.ui.UiString.Companion.invoke
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
public class EnterPointsMvvmComponent(
    @Assisted override val arguments: EnterPointsArguments,
    @Assisted private val props: StateFlow<KombioGameHostProps>,
    private val viewModelFactory: ViewModelFactory,
) :
    MvvmComponent<
        EnterPointsArguments,
        KombioGameHostProps,
        EnterPointsState,
        EnterPointsUserAction,
        EnterPointsUi,
    > {
    @Composable
    override fun Content(modifier: Modifier) {
        @Suppress("ViewModelInjection")
        val viewModel =
            viewModel(key = EnterPointsArguments.toString()) { viewModelFactory.create(props) }

        val state = viewModel.state()

        com.sebastianvm.bgcomp.mvvm.Content(
            state = state.state,
            handle = state.handle,
            uiEvents = state.uiEvents,
            ui = EnterPointsUi,
            modifier = modifier,
        )
    }

    @AssistedFactory
    @ContributesBinding(AppScope::class)
    public fun interface Factory :
        MvvmComponent.Factory<
            EnterPointsArguments,
            KombioGameHostProps,
            EnterPointsState,
            EnterPointsUserAction,
            EnterPointsUi,
        > {
        override fun create(
            arguments: EnterPointsArguments,
            props: StateFlow<KombioGameHostProps>,
        ): EnterPointsMvvmComponent
    }

    @AssistedFactory
    public fun interface ViewModelFactory {
        public fun create(props: StateFlow<KombioGameHostProps>): EnterPointsViewModel
    }
}
