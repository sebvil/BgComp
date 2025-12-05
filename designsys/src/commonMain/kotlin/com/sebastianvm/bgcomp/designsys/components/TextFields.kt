package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun OutlinedTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    androidx.compose.material3.OutlinedTextField(
        state = state,
        modifier = modifier,
        label = label?.let { { it.invoke() } },
        placeholder = placeholder,
        lineLimits =
            if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(),
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
    )
}

@Composable
fun NumberOutlinedTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    androidx.compose.material3.OutlinedTextField(
        state = state,
        modifier = modifier,
        label = label?.let { { it.invoke() } },
        placeholder = placeholder,
        lineLimits =
            if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(),
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        inputTransformation = NumberOnlyInputTransformation(),
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        onKeyboardAction = onKeyboardAction,
    )
}

class NumberOnlyInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (
            asCharSequence().any { !it.isDigit() && it != '-' } ||
                ('-' in asCharSequence() && asCharSequence().first() != '-')
        ) {
            revertAllChanges()
        }
    }
}

@Composable
fun OutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
    )
}
