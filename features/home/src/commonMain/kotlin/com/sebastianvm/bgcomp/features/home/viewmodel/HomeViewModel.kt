package com.sebastianvm.bgcomp.features.home.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.sebastianvm.bgcomp.featureinterfaces.HomeArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.home.ui.HomeUi
import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.UiEvents
import com.sebastianvm.bgcomp.mvvm.ViewModelState
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.StateFlow

@MvvmComponent(argsClass = HomeArguments::class, uiClass = HomeUi::class)
@AssistedInject
class HomeViewModel(@Assisted private val props: StateFlow<NavigationProps>) :
    BaseViewModel<NavigationProps, HomeState, HomeUserAction>(),
    HasNavigationProps by HasNavigationProps.Default(props.value) {

    @Composable
    override fun state(): ViewModelState<HomeState, HomeUserAction> {
        val uiEvents = remember { UiEvents<HomeUserAction>() }
        return ViewModelState(
            state = HomeState(games = Game.entries.toPersistentList()),
            uiEvents = uiEvents,
            handle = { action ->
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
            },
        )
    }
}
