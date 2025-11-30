package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

object IconButton {
    @Composable
    operator fun invoke(
        icon: ImageVector,
        contentDescription: String?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
    ) {
        IconButton(onClick = onClick, modifier = modifier, enabled = enabled) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}
