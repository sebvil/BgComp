package com.sebastianvm.bgcomp.features.kombio.summary.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.featureinterfaces.EnterPointsArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.features.kombio.model.KombioGameState
import com.sebastianvm.bgcomp.features.kombio.summary.ui.KombioGameSummaryUi
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@MvvmComponent(
    argsClass = KombioGameSummaryArguments::class,
    uiClass = KombioGameSummaryUi::class,
)
@AssistedInject
class KombioGameSummaryViewModel(
    @Assisted private val props: StateFlow<KombioGameHostProps>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
) :
    BaseViewModel<KombioGameHostProps, KombioGameSummaryState, KombioGameSummaryUserAction>(
        viewModelScope,
        recompositionMode,
        KombioGameSummaryState(game = props.value.game),
    ),
    HasNavigationProps by HasNavigationProps.Default(props.value.navigationProps) {

    @Composable
    override fun presenter(): KombioGameSummaryState {
        val gameState = props.collectAsState()
        return KombioGameSummaryState(game = gameState.value.game)
    }

    override fun handle(action: KombioGameSummaryUserAction) {
        when (action) {
            EnterPoints -> {
                navigateTo(EnterPointsArguments)
            }
            StartNewGame -> {
                navigateUp()
            }
        }
    }
}
