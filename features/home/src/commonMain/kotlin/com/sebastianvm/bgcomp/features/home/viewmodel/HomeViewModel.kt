package com.sebastianvm.bgcomp.features.home.viewmodel

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.featureinterfaces.HomeArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.home.ui.HomeUi
import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.StateFlow

@MvvmComponent(argsClass = HomeArguments::class, uiClass = HomeUi::class)
@AssistedInject
class HomeViewModel(
    @Assisted private val props: StateFlow<NavigationProps>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
) :
    BaseViewModel<NavigationProps, HomeState, HomeUserAction>(
        viewModelScope,
        recompositionMode,
        HomeState(persistentListOf()),
    ),
    HasNavigationProps by HasNavigationProps.Default(props.value) {

    @Composable
    override fun presenter(): HomeState {
        return HomeState(games = Game.entries.toPersistentList())
    }


    override fun handle(action: HomeUserAction) {
        when (action) {
            is GameClicked -> {
                when (action.game) {
                    Game.Azul -> TODO()
                    Game.Kombio -> {
                        navigateTo(NewKombioGameArguments)
                    }
                }
            }
        }
    }
}
