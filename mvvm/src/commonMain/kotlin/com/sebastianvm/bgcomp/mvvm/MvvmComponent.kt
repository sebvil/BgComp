package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import com.sebastianvm.bgcomp.designsys.components.AlertDialog
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.cancel_verb
import com.sebastianvm.bgcomp.resources.ok
import com.sebastianvm.bgcomp.ui.UiString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.modules.SerializersModule

typealias GenericMvvmComponent = MvvmComponent<*, *, *, *, *>

interface MvvmComponent<
    Args : MvvmComponentArguments,
    P : Props,
    S : UiState,
    A : UserAction,
    U : Ui<S, A>,
> {
    val arguments: Args

    @Composable fun Content(modifier: Modifier = Modifier)

    interface Factory<
        Args : MvvmComponentArguments,
        P : Props,
        S : UiState,
        A : UserAction,
        U : Ui<S, A>,
    > {
        fun create(arguments: Args, props: StateFlow<P>): MvvmComponent<Args, P, S, A, U>
    }

    interface NoPropsFactory<
        Args : MvvmComponentArguments,
        S : UiState,
        A : UserAction,
        U : Ui<S, A>,
    > {
        fun create(arguments: Args): MvvmComponent<Args, Nothing, S, A, U>
    }
}

@Composable
inline fun <
    reified Args : MvvmComponentArguments,
    P : Props,
    S : UiState,
    A : UserAction,
    U : Ui<S, A>,
> Content(
    arguments: Args,
    ui: U,
    crossinline initializeViewModel: () -> BaseViewModel<P, S, A>,
    modifier: Modifier = Modifier,
) {
    @Suppress("ViewModelInjection")
    val viewModel = viewModel(key = arguments.toString()) { initializeViewModel() }
    Content(viewModel = viewModel, ui = ui, modifier = modifier)
}

@Composable
inline fun <
    reified Args : MvvmComponentArguments,
    P : Props,
    S : UiState,
    A : UserAction,
    U : Ui<S, A>,
    reified SS : SavedState,
> SavedStateContent(
    arguments: Args,
    ui: U,
    crossinline initializeViewModel: (MutableStateFlow<SS>) -> BaseViewModel<P, S, A>,
    initialSavedState: SS,
    modifier: Modifier = Modifier,
) {
    val currentSerializersModule = LocalSerializersModule.current
    val saveStateConfig = SavedStateConfiguration {
        this.serializersModule = currentSerializersModule
    }

    @Suppress("ViewModelInjection")
    val viewModel =
        viewModel(key = arguments.toString()) {
            val handle = createSavedStateHandle()
            val savedState =
                MutableStateFlow(
                    handle.get<androidx.savedstate.SavedState>("savedState")?.let {
                        decodeFromSavedState<SS>(it, configuration = saveStateConfig)
                    } ?: initialSavedState
                )
            handle.setSavedStateProvider("savedState") {
                encodeToSavedState(savedState.value, configuration = saveStateConfig)
            }
            initializeViewModel(savedState)
        }
    Content(viewModel = viewModel, ui = ui, modifier = modifier)
}

@Composable
fun <P : Props, S : UiState, A : UserAction, U : Ui<S, A>> Content(
    viewModel: BaseViewModel<P, S, A>,
    ui: U,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    val uiEvents = viewModel.uiEvents

    ui(state = state, handle = viewModel::handle, modifier = modifier)

    uiEvents.alertDialog?.let { event ->
        AlertDialog(
            title = event.title,
            message = event.message,
            confirmButtonText = event.confirmButtonState?.text ?: UiString(Res.string.ok),
            onConfirm = { event.confirmButtonState?.action?.let { viewModel.handle(it) } },
            dismissButtonText = event.dismissButtonState?.text ?: UiString(Res.string.cancel_verb),
            onDismiss = event.dismissButtonState?.action?.let { { viewModel.handle(it) } },
            onDismissRequest = { uiEvents.alertDialog = null },
        )
    }
}

val LocalSerializersModule = compositionLocalOf { SerializersModule {} }
