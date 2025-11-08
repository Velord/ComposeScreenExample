package com.velord.core.ui.compose.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph
import com.velord.core.ui.compose.path.toComposePath

class MorphShape(
    private val morph: Morph,
    private val progress: Float,
    private val rotationZ: Float = 0f
) : Shape {

    private val matrix = Matrix()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // Below assumes that you haven't changed the default radius of 1f, nor the centerX and centerY of 0f
        // By default this stretches the path to the size of the container, if you don't want stretching, use the same size.width for both x and y.
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        matrix.rotateZ(rotationZ)

        val path = morph.toComposePath(progress = progress)
        path.transform(matrix)
        return Outline.Generic(path)
    }
}