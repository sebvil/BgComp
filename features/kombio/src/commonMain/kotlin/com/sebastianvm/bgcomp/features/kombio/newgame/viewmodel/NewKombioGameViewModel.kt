package com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameHostArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.kombio.newgame.ui.NewKombioGameUi
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.StateProducerScope
import com.sebastianvm.bgcomp.mvvm.ViewModelState
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.mvvm.rememberSerializable
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.StateFlow

@MvvmComponent(argsClass = NewKombioGameArguments::class, uiClass = NewKombioGameUi::class)
@AssistedInject
class NewKombioGameViewModel(@Assisted private val props: StateFlow<NavigationProps>) :
    BaseViewModel<NavigationProps, NewKombioGameState, NewKombioGameUserAction>(),
    HasNavigationProps by HasNavigationProps.Default(props.value) {

    @Composable
    override fun StateProducerScope<NewKombioGameState, NewKombioGameUserAction>.state():
        ViewModelState<NewKombioGameState, NewKombioGameUserAction> {
        val playerNames =
            rememberSaveable(
                saver =
                    Saver(
                        save = { textFields ->
                            textFields.map { with(TextFieldState.Saver) { save(it) } }
                        },
                        restore = { value ->
                            value
                                .filterNotNull()
                                .mapNotNull { TextFieldState.Saver.restore(it) }
                                .toMutableStateList()
                        },
                    )
            ) {
                mutableStateListOf(TextFieldState(), TextFieldState())
            }
        var gameMode: GameMode by rememberSerializable { mutableStateOf(GameMode.Points()) }

        return createState(
            state = NewKombioGameState(playerNames, gameMode),
            handle = { action ->
                when (action) {
                    is GameModeDataUpdated -> {
                        gameMode = action.newGameModeData
                    }

                    is GameModeSelected -> {
                        gameMode =
                            when (action.mode) {
                                GameModeType.Points -> GameMode.Points()
                                GameModeType.Rounds -> GameMode.Rounds()
                            }
                    }

                    PlayerAdded -> {
                        playerNames.add(TextFieldState())
                    }

                    PlayerRemoved -> {
                        playerNames.removeAt(playerNames.lastIndex)
                    }

                    StartGame -> {
                        navigateTo(
                            KombioGameHostArguments(
                                gameMode = gameMode,
                                playerNames = playerNames.map { it.text.toString() },
                            )
                        )
                    }
                }
            },
        )
    }
}
