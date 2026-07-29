package com.velord.core.ui.compose.path

import androidx.compose.ui.graphics.vector.PathBuilder

fun PathBuilder.doubleArrowRightPath() {
    moveTo(2.5f, 10f)
    lineTo(11.5f, 10f)
    moveTo(7.5f, 6f)
    lineTo(11.5f, 10f)
    lineTo(7.5f, 14f)
    moveTo(13f, 6f)
    lineTo(17f, 10f)
    lineTo(13f, 14f)
}
