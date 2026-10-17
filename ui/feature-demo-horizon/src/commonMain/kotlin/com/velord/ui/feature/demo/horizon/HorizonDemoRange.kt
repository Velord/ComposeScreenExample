package com.velord.ui.feature.demo.horizon

/**
 * Editing ranges for the Horizon Bloom demo, not limits of the rendering library.
 * Consumers can supply values outside these ranges through HorizonBloomConfig.
 */
internal object HorizonDemoRange {
    private val UNIT = 0f..1f
    private val STRENGTH = 0f..2f

    val HORIZON = UNIT
    val CURVATURE = STRENGTH
    val TILT = -30f..30f
    val SUN_POSITION = -1f..1f
    val SUN_HEIGHT = UNIT
    val SUN_SIZE = STRENGTH
    val SUNRISE = STRENGTH
    val SPREAD = STRENGTH
    val ATMOSPHERE = STRENGTH
    val THICKNESS = STRENGTH
    val RIM = STRENGTH
    val FLARE = STRENGTH
    val SUNBURST = STRENGTH
    val SUNBURST_RAY_COUNT = 4..16
    val AIRGLOW = STRENGTH
    val CLOUDS = STRENGTH
    val STARS = STRENGTH
    val AURORA = STRENGTH
    val AURORA_SPEED = STRENGTH
    val SPEED = STRENGTH
    val DRIFT = STRENGTH
    val PARALLAX = STRENGTH
    val BLOOM = STRENGTH
    val GRAIN = UNIT
}
