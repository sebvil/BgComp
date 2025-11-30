@file:Suppress("MagicNumber")

package com.sebastianvm.bgcomp.designsys.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.IconAdd: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "IconAdd",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            )
            .apply {
                path(fill = SolidColor(Color.White)) {
                    moveTo(440f, 520f)
                    lineTo(200f, 520f)
                    lineTo(200f, 440f)
                    lineTo(440f, 440f)
                    lineTo(440f, 200f)
                    lineTo(520f, 200f)
                    lineTo(520f, 440f)
                    lineTo(760f, 440f)
                    lineTo(760f, 520f)
                    lineTo(520f, 520f)
                    lineTo(520f, 760f)
                    lineTo(440f, 760f)
                    lineTo(440f, 520f)
                    close()
                }
            }
            .build()
    }
