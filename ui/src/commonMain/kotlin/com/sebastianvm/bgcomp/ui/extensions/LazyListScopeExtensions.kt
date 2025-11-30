package com.sebastianvm.bgcomp.ui.extensions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

context(lazyListScope: LazyListScope)
fun horizontalSpacerItem(key: Any, height: Dp = 16.dp) =
    with(lazyListScope) {
        item(key = key, contentType = "Spacer") { Spacer(modifier = Modifier.height(height)) }
    }
