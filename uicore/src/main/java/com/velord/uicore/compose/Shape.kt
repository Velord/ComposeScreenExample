package com.velord.uicore.compose

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.path.createTicketPath
import com.velord.uicore.compose.path.pervasiveArcFromBottomPath

val MainShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

fun TicketShape(cornerRadius: Float): Shape =
    GenericShape { size, _ ->
        addPath(createTicketPath(cornerRadius, size))
    }

fun PervasiveArcFromBottomShape(
    progress: Int,
    onEdgeTouch: () -> Unit
) : Shape = GenericShape { size, _ ->
    addPath(pervasiveArcFromBottomPath(size, progress, onEdgeTouch))
}