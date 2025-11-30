@file:Suppress("MagicNumber")

package com.sebastianvm.bgcomp.designsys.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.IconChevronRight: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "IconChevronRight",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
                autoMirror = true,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(504f, 480f)
                    lineTo(320f, 296f)
                    lineTo(376f, 240f)
                    lineTo(616f, 480f)
                    lineTo(376f, 720f)
                    lineTo(320f, 664f)
                    lineTo(504f, 480f)
                    close()
                }
            }
            .build()
    }
