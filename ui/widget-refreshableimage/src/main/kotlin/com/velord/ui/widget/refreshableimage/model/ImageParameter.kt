package com.velord.ui.widget.refreshableimage.model

import android.os.Parcelable
import androidx.compose.ui.unit.DpSize
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ImageParameter(
    val seed: String,
    val width: Float,
    val height: Float
) : Parcelable {

    constructor(seed: String, size: DpSize) :
            this(seed, width = size.width.value, height = size.height.value)

    override fun toString(): String = "Seed = $seed x Width = $width x Height=$height"

    fun getSimpleWidth() = width.toInt()

    fun getSimpleHeight() = height.toInt()

    companion object {
        const val DEFAULT_SEED = "seed"
    }
}
