@file:Suppress("MagicNumber")

package com.sebastianvm.bgcomp.designsys.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.IconChevronDown: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "IconChevronDown",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(480f, 616f)
                    lineTo(240f, 376f)
                    lineTo(296f, 320f)
                    lineTo(480f, 504f)
                    lineTo(664f, 320f)
                    lineTo(720f, 376f)
                    lineTo(480f, 616f)
                    close()
                }
            }
            .build()
    }
