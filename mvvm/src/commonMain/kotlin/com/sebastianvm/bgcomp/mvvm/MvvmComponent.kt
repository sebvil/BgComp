package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sebastianvm.bgcomp.designsys.components.AlertDialog
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.cancel_verb
import com.sebastianvm.bgcomp.resources.ok
import com.sebastianvm.bgcomp.ui.UiString
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
fun <S : UiState, A : UserAction, U : Ui<S, A>> Content(
    state: S,
    handle: (A) -> Unit,
    uiEvents: UiEvents<A>,
    ui: U,
    modifier: Modifier = Modifier,
) {

    ui(state = state, handle = handle, modifier = modifier)

    uiEvents.alertDialog?.let { event ->
        AlertDialog(
            title = event.title,
            message = event.message,
            confirmButtonText = event.confirmButtonState?.text ?: UiString(Res.string.ok),
            onConfirm = { event.confirmButtonState?.action?.let { handle(it) } },
            dismissButtonText = event.dismissButtonState?.text ?: UiString(Res.string.cancel_verb),
            onDismiss = event.dismissButtonState?.action?.let { { handle(it) } },
            onDismissRequest = { uiEvents.alertDialog = null },
        )
    }
}

val LocalSerializersModule = compositionLocalOf { SerializersModule {} }
