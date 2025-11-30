package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object ListItemDefaults {
    @Composable
    fun colors(containerColor: Color = Color.Transparent): ListItemColors =
        ListItemColors(containerColor = containerColor)
}

data class ListItemColors(val containerColor: Color)

@Composable
fun ListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
) {
    androidx.compose.material3.ListItem(
        modifier = modifier,
        leadingContent = leadingContent,
        overlineContent = overlineContent,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        colors =
            androidx.compose.material3.ListItemDefaults.colors(
                containerColor = colors.containerColor
            ),
    )
}
