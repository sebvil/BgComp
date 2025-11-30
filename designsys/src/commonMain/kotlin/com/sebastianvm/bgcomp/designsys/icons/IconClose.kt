@file:Suppress("MagicNumber")

package com.sebastianvm.bgcomp.designsys.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.IconClose: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "IconClose",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(256f, 760f)
                    lineTo(200f, 704f)
                    lineTo(424f, 480f)
                    lineTo(200f, 256f)
                    lineTo(256f, 200f)
                    lineTo(480f, 424f)
                    lineTo(704f, 200f)
                    lineTo(760f, 256f)
                    lineTo(536f, 480f)
                    lineTo(760f, 704f)
                    lineTo(704f, 760f)
                    lineTo(480f, 536f)
                    lineTo(256f, 760f)
                    close()
                }
            }
            .build()
    }
