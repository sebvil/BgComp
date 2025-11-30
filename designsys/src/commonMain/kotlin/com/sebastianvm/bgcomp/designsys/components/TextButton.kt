package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.sebastianvm.bgcomp.ui.UiString
import org.jetbrains.compose.resources.StringResource

object TextButton {

    @Composable
    operator fun invoke(
        text: StringResource,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        icon: ImageVector? = null,
    ) {
        TextButton(
            text = UiString(text),
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            icon = icon,
        )
    }

    @Composable
    operator fun invoke(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        icon: ImageVector? = null,
    ) {
        TextButton(
            text = UiString(text),
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            icon = icon,
        )
    }

    @Composable
    operator fun invoke(
        text: UiString,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        icon: ImageVector? = null,
    ) {
        TextButton(onClick = onClick, modifier = modifier, enabled = enabled) {
            icon?.let { Icon(imageVector = it, contentDescription = null) }
            Text(text)
        }
    }
}
