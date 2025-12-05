package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.cancel_verb
import com.sebastianvm.bgcomp.ui.UiString

@Stable
class UiEvents<A : UserAction> {
    var alertDialog: ShowAlertDialog<A>? by mutableStateOf(null)
}

data class ShowAlertDialog<A : UserAction>(
    val title: UiString,
    val message: UiString,
    val confirmButtonState: ButtonState<A>?,
    val dismissButtonState: ButtonState<A>? =
        ButtonState(text = UiString(Res.string.cancel_verb), action = null),
) {

    data class ButtonState<A>(val text: UiString, val action: A?)
}
