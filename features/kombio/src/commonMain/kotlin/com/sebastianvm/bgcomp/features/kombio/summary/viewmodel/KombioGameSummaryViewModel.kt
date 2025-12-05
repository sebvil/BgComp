package com.sebastianvm.bgcomp.features.kombio.summary.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sebastianvm.bgcomp.featureinterfaces.EnterPointsArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.features.kombio.summary.ui.KombioGameSummaryUi
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.StateProducerScope
import com.sebastianvm.bgcomp.mvvm.ViewModelState
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.StateFlow

@MvvmComponent(argsClass = KombioGameSummaryArguments::class, uiClass = KombioGameSummaryUi::class)
@AssistedInject
class KombioGameSummaryViewModel(@Assisted private val props: StateFlow<KombioGameHostProps>) :
    BaseViewModel<KombioGameHostProps, KombioGameSummaryState, KombioGameSummaryUserAction>(),
    HasNavigationProps by HasNavigationProps.Default(props.value.navigationProps) {

    @Composable
    override fun StateProducerScope<KombioGameSummaryState, KombioGameSummaryUserAction>.state():
        ViewModelState<KombioGameSummaryState, KombioGameSummaryUserAction> {
        val propsValue by props.collectAsState()
        return createState(
            state = KombioGameSummaryState(game = propsValue.game),
            handle = { action ->
                when (action) {
                    EnterPoints -> {
                        navigateTo(EnterPointsArguments)
                    }

                    StartNewGame -> {
                        navigateUp()
                    }
                }
            },
        )
    }
}
