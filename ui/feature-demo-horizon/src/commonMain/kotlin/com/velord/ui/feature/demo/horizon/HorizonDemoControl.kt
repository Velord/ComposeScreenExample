package com.velord.ui.feature.demo.horizon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.horizonbloom.HorizonBloomConfig
import kotlin.math.roundToInt

@Composable
internal fun HorizonControlSection(
    config: HorizonBloomConfig,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonControl(
        value = config.horizon,
        onAction = onAction,
    )
    CurvatureControl(
        value = config.curvature,
        onAction = onAction,
    )
    TiltControl(
        value = config.tilt,
        onAction = onAction,
    )
    SunPositionControl(
        value = config.sunPosition,
        onAction = onAction,
    )
    SunHeightControl(
        value = config.sunHeight,
        onAction = onAction,
    )
    SunSizeControl(
        value = config.sunSize,
        onAction = onAction,
    )
    SunriseControl(
        value = config.sunrise,
        onAction = onAction,
    )
    SpreadControl(
        value = config.spread,
        onAction = onAction,
    )
    AtmosphereControl(
        value = config.atmosphere,
        onAction = onAction,
    )
    ThicknessControl(
        value = config.thickness,
        onAction = onAction,
    )
    RimControl(
        value = config.rim,
        onAction = onAction,
    )
    FlareControl(
        value = config.flare,
        onAction = onAction,
    )
    SunburstControl(
        value = config.sunburst,
        onAction = onAction,
    )
    SunburstRayCountControl(
        value = config.sunburstRayCount,
        onAction = onAction,
    )
    AirglowControl(
        value = config.airglow,
        onAction = onAction,
    )
    CloudsControl(
        value = config.clouds,
        onAction = onAction,
    )
    StarsControl(
        value = config.stars,
        onAction = onAction,
    )
    AuroraControl(
        value = config.aurora,
        onAction = onAction,
    )
    AuroraSpeedControl(
        value = config.auroraSpeed,
        onAction = onAction,
    )
    SpeedControl(
        value = config.speed,
        onAction = onAction,
    )
    DriftControl(
        value = config.drift,
        onAction = onAction,
    )
    ParallaxControl(
        value = config.parallax,
        onAction = onAction,
    )
    BloomControl(
        value = config.bloom,
        onAction = onAction,
    )
    GrainControl(
        value = config.grain,
        onAction = onAction,
    )
}

@Composable
private fun HorizonControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Horizon",
        value = value,
        range = HorizonDemoRange.HORIZON,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeHorizon(it)) },
    )
}

@Composable
private fun CurvatureControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Curvature",
        value = value,
        range = HorizonDemoRange.CURVATURE,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeCurvature(it)) },
    )
}

@Composable
private fun TiltControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Tilt (°)",
        value = value,
        range = HorizonDemoRange.TILT,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeTilt(it)) },
    )
}

@Composable
private fun SunPositionControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Sun position",
        value = value,
        range = HorizonDemoRange.SUN_POSITION,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSunPosition(it)) },
    )
}

@Composable
private fun SunHeightControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Sun height",
        value = value,
        range = HorizonDemoRange.SUN_HEIGHT,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSunHeight(it)) },
    )
}

@Composable
private fun SunSizeControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Sun size",
        value = value,
        range = HorizonDemoRange.SUN_SIZE,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSunSize(it)) },
    )
}

@Composable
private fun SunriseControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Sunrise",
        value = value,
        range = HorizonDemoRange.SUNRISE,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSunrise(it)) },
    )
}

@Composable
private fun SpreadControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Spread",
        value = value,
        range = HorizonDemoRange.SPREAD,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSpread(it)) },
    )
}

@Composable
private fun AtmosphereControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Atmosphere",
        value = value,
        range = HorizonDemoRange.ATMOSPHERE,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeAtmosphere(it)) },
    )
}

@Composable
private fun ThicknessControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Thickness",
        value = value,
        range = HorizonDemoRange.THICKNESS,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeThickness(it)) },
    )
}

@Composable
private fun RimControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Rim",
        value = value,
        range = HorizonDemoRange.RIM,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeRim(it)) },
    )
}

@Composable
private fun FlareControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Flare",
        value = value,
        range = HorizonDemoRange.FLARE,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeFlare(it)) },
    )
}

@Composable
private fun SunburstControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Sunburst",
        value = value,
        range = HorizonDemoRange.SUNBURST,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSunburst(it)) },
    )
}

@Composable
private fun SunburstRayCountControl(
    value: Int,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    val range = HorizonDemoRange.SUNBURST_RAY_COUNT
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Sunburst ray count: $value",
            style = MaterialTheme.typography.bodySmall,
        )
        Slider(
            modifier = Modifier.height(40.dp),
            value = value.coerceIn(range).toFloat(),
            onValueChange = {
                onAction(HorizonDemoUiAction.SetSunburstCount(it.roundToInt()))
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.last - range.first - 1,
        )
    }
}

@Composable
private fun AirglowControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Airglow",
        value = value,
        range = HorizonDemoRange.AIRGLOW,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeAirglow(it)) },
    )
}

@Composable
private fun CloudsControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Clouds",
        value = value,
        range = HorizonDemoRange.CLOUDS,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeClouds(it)) },
    )
}

@Composable
private fun StarsControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Stars",
        value = value,
        range = HorizonDemoRange.STARS,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeStars(it)) },
    )
}

@Composable
private fun AuroraControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Aurora",
        value = value,
        range = HorizonDemoRange.AURORA,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeAurora(it)) },
    )
}

@Composable
private fun AuroraSpeedControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Aurora speed",
        value = value,
        range = HorizonDemoRange.AURORA_SPEED,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeAuroraSpeed(it)) },
    )
}

@Composable
private fun SpeedControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Speed",
        value = value,
        range = HorizonDemoRange.SPEED,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeSpeed(it)) },
    )
}

@Composable
private fun DriftControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Drift",
        value = value,
        range = HorizonDemoRange.DRIFT,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeDrift(it)) },
    )
}

@Composable
private fun ParallaxControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Parallax (drag preview)",
        value = value,
        range = HorizonDemoRange.PARALLAX,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeParallax(it)) },
    )
}

@Composable
private fun BloomControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Bloom",
        value = value,
        range = HorizonDemoRange.BLOOM,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeBloom(it)) },
    )
}

@Composable
private fun GrainControl(
    value: Float,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonSlider(
        label = "Grain",
        value = value,
        range = HorizonDemoRange.GRAIN,
        onValueChange = { onAction(HorizonDemoUiAction.ChangeGrain(it)) },
    )
}

@Composable
internal fun HorizonSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "$label: ${(value * 100).toInt() / 100f}",
            style = MaterialTheme.typography.bodySmall,
        )
        Slider(
            modifier = Modifier.height(40.dp),
            value = value.coerceIn(range.start, range.endInclusive),
            onValueChange = onValueChange,
            valueRange = range,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Column(
        modifier = Modifier
            .height(520.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        HorizonControlSection(
            config = HorizonBloomConfig.DAWN,
            onAction = {},
        )
    }
}
