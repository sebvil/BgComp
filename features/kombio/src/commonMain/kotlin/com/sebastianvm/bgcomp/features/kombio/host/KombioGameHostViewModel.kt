package com.sebastianvm.bgcomp.features.kombio.host

import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameHostArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.features.kombio.model.KombioGame
import com.sebastianvm.bgcomp.features.kombio.model.PlayerState
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import com.sebastianvm.bgcomp.navigation.viewmodel.BaseNavHostViewModel
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostArguments
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostSavedState
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


@MvvmComponent(
    argsClass = KombioGameHostArguments::class,
    uiClass = NavHostUi::class,
    savedStateClasses = [NavHostSavedState::class, KombioGame::class],
)
@AssistedInject
class KombioGameHostViewModel(
    @Assisted private val arguments: KombioGameHostArguments,
    @Assisted private val savedStateFlow: MutableStateFlow<NavHostSavedState>,
    @Assisted private val savedGame: MutableStateFlow<KombioGame>,
    @Assisted private val props: StateFlow<NavigationProps>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
    mvvmComponentInitializer: MvvmComponentInitializer<KombioGameHostProps>,
) : BaseNavHostViewModel<NavigationProps, KombioGameHostProps>(
    arguments = NavHostArguments(args = KombioGameSummaryArguments),
    savedStateFlow = savedStateFlow,
    viewModelScope = viewModelScope,
    recompositionMode = recompositionMode,
    mvvmComponentInitializer = mvvmComponentInitializer,
    parentNavProps = props.value
) {
    init {
        if (!savedGame.value.isRestored) {
            savedGame.update {
                it.copy(
                    players = arguments.playerNames.map { name -> PlayerState(name) }.toPersistentList(),
                    gameMode = arguments.gameMode
                )
            }
        }
    }
    override val childrenProps: StateFlow<KombioGameHostProps> = savedGame.mapAsStateFlow {
        KombioGameHostProps(
            game = it,
            navigationProps = navigationProps,
            updateGame = { newSavedGame -> savedGame.value = newSavedGame })
    }
}