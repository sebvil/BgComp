package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.bgcomp.ui.UiString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    androidx.compose.material3.BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        content = content,
        modifier = modifier,
    )
}

@Composable
fun AlertDialog(
    title: UiString,
    message: UiString,
    confirmButtonText: UiString,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: UiString? = null,
    onDismiss: (() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = { Text(text = message, maxLines = Int.MAX_VALUE) },
        confirmButton = { TextButton(text = confirmButtonText, onClick = onConfirm) },
        dismissButton =
            dismissButtonText?.let {
                { TextButton(text = it, onClick = onDismiss ?: onDismissRequest) }
            },
        modifier = modifier,
    )
}
