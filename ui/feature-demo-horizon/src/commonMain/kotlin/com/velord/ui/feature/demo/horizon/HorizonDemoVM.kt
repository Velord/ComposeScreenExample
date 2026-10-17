package com.velord.ui.feature.demo.horizon

import androidx.compose.ui.graphics.Color
import com.velord.horizonbloom.HorizonBloomConfig
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HorizonDemoUiState(val config: HorizonBloomConfig) {

    companion object {
        val INIT = HorizonDemoUiState(config = HorizonBloomConfig.DAWN)
    }
}

sealed interface HorizonDemoUiAction {
    data object Reset : HorizonDemoUiAction
    data object SelectDawn : HorizonDemoUiAction
    data object SelectTwilight : HorizonDemoUiAction
    data object SelectAurora : HorizonDemoUiAction
    data class ChangeHorizon(val value: Float) : HorizonDemoUiAction
    data class ChangeCurvature(val value: Float) : HorizonDemoUiAction
    data class ChangeTilt(val value: Float) : HorizonDemoUiAction
    data class ChangeSunPosition(val value: Float) : HorizonDemoUiAction
    data class ChangeSunHeight(val value: Float) : HorizonDemoUiAction
    data class ChangeSunSize(val value: Float) : HorizonDemoUiAction
    data class ChangeSunrise(val value: Float) : HorizonDemoUiAction
    data class ChangeSpread(val value: Float) : HorizonDemoUiAction
    data class ChangeAtmosphere(val value: Float) : HorizonDemoUiAction
    data class ChangeThickness(val value: Float) : HorizonDemoUiAction
    data class ChangeRim(val value: Float) : HorizonDemoUiAction
    data class ChangeFlare(val value: Float) : HorizonDemoUiAction
    data class ChangeSunburst(val value: Float) : HorizonDemoUiAction
    data class SetSunburstCount(val value: Int) : HorizonDemoUiAction
    data class ChangeAirglow(val value: Float) : HorizonDemoUiAction
    data class ChangeClouds(val value: Float) : HorizonDemoUiAction
    data class ChangeStars(val value: Float) : HorizonDemoUiAction
    data class ChangeAurora(val value: Float) : HorizonDemoUiAction
    data class ChangeAuroraSpeed(val value: Float) : HorizonDemoUiAction
    data class ChangeSpeed(val value: Float) : HorizonDemoUiAction
    data class ChangeDrift(val value: Float) : HorizonDemoUiAction
    data class ChangeParallax(val value: Float) : HorizonDemoUiAction
    data class ChangeBloom(val value: Float) : HorizonDemoUiAction
    data class ChangeGrain(val value: Float) : HorizonDemoUiAction
    data class ChangeWarmColor(val value: Color) : HorizonDemoUiAction
    data class ChangeCoolColor(val value: Color) : HorizonDemoUiAction
    data class ChangeBackground(val value: Color) : HorizonDemoUiAction
    data class SetAutoAurora(val value: Boolean) : HorizonDemoUiAction
    data class SetInteractive(val value: Boolean) : HorizonDemoUiAction
    data class SetIntro(val value: Boolean) : HorizonDemoUiAction
    data class SetPaused(val value: Boolean) : HorizonDemoUiAction
}

class HorizonDemoVM : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(HorizonDemoUiState.INIT)
    private val actionFlow = MutableSharedFlow<HorizonDemoUiAction>()

    init {
        observe()
    }

    fun onAction(action: HorizonDemoUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onReset() {
        uiStateFlow.update { state -> state.copy(config = HorizonBloomConfig.DAWN) }
    }

    private fun onSelectDawn() {
        uiStateFlow.update { state -> state.copy(config = HorizonBloomConfig.DAWN) }
    }

    private fun onSelectTwilight() {
        uiStateFlow.update { state -> state.copy(config = HorizonBloomConfig.TWILIGHT) }
    }

    private fun onSelectAurora() {
        uiStateFlow.update { state -> state.copy(config = HorizonBloomConfig.AURORA) }
    }

    private fun onChangeHorizon(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(horizon = value.coerceIn(HorizonDemoRange.HORIZON)))
        }
    }

    private fun onChangeCurvature(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(curvature = value.coerceIn(HorizonDemoRange.CURVATURE)))
        }
    }

    private fun onChangeTilt(value: Float) {
        uiStateFlow.update { state ->
            val boundedTilt = value.coerceIn(HorizonDemoRange.TILT)
            state.copy(config = state.config.copy(tilt = boundedTilt))
        }
    }

    private fun onChangeSunPosition(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(sunPosition = value.coerceIn(HorizonDemoRange.SUN_POSITION)))
        }
    }

    private fun onChangeSunHeight(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(sunHeight = value.coerceIn(HorizonDemoRange.SUN_HEIGHT)))
        }
    }

    private fun onChangeSunSize(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(sunSize = value.coerceIn(HorizonDemoRange.SUN_SIZE)))
        }
    }

    private fun onChangeSunrise(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(sunrise = value.coerceIn(HorizonDemoRange.SUNRISE)))
        }
    }

    private fun onChangeSpread(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(spread = value.coerceIn(HorizonDemoRange.SPREAD)))
        }
    }

    private fun onChangeAtmosphere(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(atmosphere = value.coerceIn(HorizonDemoRange.ATMOSPHERE)))
        }
    }

    private fun onChangeThickness(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(thickness = value.coerceIn(HorizonDemoRange.THICKNESS)))
        }
    }

    private fun onChangeRim(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(rim = value.coerceIn(HorizonDemoRange.RIM)))
        }
    }

    private fun onChangeFlare(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(flare = value.coerceIn(HorizonDemoRange.FLARE)))
        }
    }

    private fun onChangeSunburst(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(sunburst = value.coerceIn(HorizonDemoRange.SUNBURST)))
        }
    }

    private fun onSetSunburstCount(value: Int) {
        uiStateFlow.update { state ->
            val count = value.coerceIn(HorizonDemoRange.SUNBURST_RAY_COUNT)
            state.copy(config = state.config.copy(sunburstRayCount = count))
        }
    }

    private fun onChangeAirglow(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(airglow = value.coerceIn(HorizonDemoRange.AIRGLOW)))
        }
    }

    private fun onChangeClouds(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(clouds = value.coerceIn(HorizonDemoRange.CLOUDS)))
        }
    }

    private fun onChangeStars(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(stars = value.coerceIn(HorizonDemoRange.STARS)))
        }
    }

    private fun onChangeAurora(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(aurora = value.coerceIn(HorizonDemoRange.AURORA)))
        }
    }

    private fun onChangeAuroraSpeed(value: Float) {
        uiStateFlow.update { state ->
            state.copy(
                config = state.config.copy(
                    auroraSpeed = value.coerceIn(HorizonDemoRange.AURORA_SPEED),
                ),
            )
        }
    }

    private fun onChangeSpeed(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(speed = value.coerceIn(HorizonDemoRange.SPEED)))
        }
    }

    private fun onChangeDrift(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(drift = value.coerceIn(HorizonDemoRange.DRIFT)))
        }
    }

    private fun onChangeParallax(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(parallax = value.coerceIn(HorizonDemoRange.PARALLAX)))
        }
    }

    private fun onChangeBloom(value: Float) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(bloom = value.coerceIn(HorizonDemoRange.BLOOM)))
        }
    }

    private fun onChangeGrain(value: Float) {
        val boundedGrain = value.coerceIn(HorizonDemoRange.GRAIN)
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(grain = boundedGrain))
        }
    }

    private fun onChangeWarmColor(value: Color) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(warmColor = value))
        }
    }

    private fun onChangeCoolColor(value: Color) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(coolColor = value))
        }
    }

    private fun onChangeBackground(value: Color) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(backgroundColor = value))
        }
    }

    private fun onSetAutoAurora(value: Boolean) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(autoAurora = value))
        }
    }

    private fun onSetInteractive(value: Boolean) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(interactive = value))
        }
    }

    private fun onSetIntro(value: Boolean) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(intro = value))
        }
    }

    private fun onSetPaused(value: Boolean) {
        uiStateFlow.update { state ->
            state.copy(config = state.config.copy(paused = value))
        }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    HorizonDemoUiAction.Reset -> onReset()
                    HorizonDemoUiAction.SelectDawn -> onSelectDawn()
                    HorizonDemoUiAction.SelectTwilight -> onSelectTwilight()
                    HorizonDemoUiAction.SelectAurora -> onSelectAurora()
                    is HorizonDemoUiAction.ChangeHorizon -> onChangeHorizon(action.value)
                    is HorizonDemoUiAction.ChangeCurvature -> onChangeCurvature(action.value)
                    is HorizonDemoUiAction.ChangeTilt -> onChangeTilt(action.value)
                    is HorizonDemoUiAction.ChangeSunPosition -> onChangeSunPosition(action.value)
                    is HorizonDemoUiAction.ChangeSunHeight -> onChangeSunHeight(action.value)
                    is HorizonDemoUiAction.ChangeSunSize -> onChangeSunSize(action.value)
                    is HorizonDemoUiAction.ChangeSunrise -> onChangeSunrise(action.value)
                    is HorizonDemoUiAction.ChangeSpread -> onChangeSpread(action.value)
                    is HorizonDemoUiAction.ChangeAtmosphere -> onChangeAtmosphere(action.value)
                    is HorizonDemoUiAction.ChangeThickness -> onChangeThickness(action.value)
                    is HorizonDemoUiAction.ChangeRim -> onChangeRim(action.value)
                    is HorizonDemoUiAction.ChangeFlare -> onChangeFlare(action.value)
                    is HorizonDemoUiAction.ChangeSunburst -> onChangeSunburst(action.value)
                    is HorizonDemoUiAction.SetSunburstCount -> onSetSunburstCount(action.value)
                    is HorizonDemoUiAction.ChangeAirglow -> onChangeAirglow(action.value)
                    is HorizonDemoUiAction.ChangeClouds -> onChangeClouds(action.value)
                    is HorizonDemoUiAction.ChangeStars -> onChangeStars(action.value)
                    is HorizonDemoUiAction.ChangeAurora -> onChangeAurora(action.value)
                    is HorizonDemoUiAction.ChangeAuroraSpeed -> onChangeAuroraSpeed(action.value)
                    is HorizonDemoUiAction.ChangeSpeed -> onChangeSpeed(action.value)
                    is HorizonDemoUiAction.ChangeDrift -> onChangeDrift(action.value)
                    is HorizonDemoUiAction.ChangeParallax -> onChangeParallax(action.value)
                    is HorizonDemoUiAction.ChangeBloom -> onChangeBloom(action.value)
                    is HorizonDemoUiAction.ChangeGrain -> onChangeGrain(action.value)
                    is HorizonDemoUiAction.ChangeWarmColor -> onChangeWarmColor(action.value)
                    is HorizonDemoUiAction.ChangeCoolColor -> onChangeCoolColor(action.value)
                    is HorizonDemoUiAction.ChangeBackground -> onChangeBackground(action.value)
                    is HorizonDemoUiAction.SetAutoAurora -> onSetAutoAurora(action.value)
                    is HorizonDemoUiAction.SetInteractive -> onSetInteractive(action.value)
                    is HorizonDemoUiAction.SetIntro -> onSetIntro(action.value)
                    is HorizonDemoUiAction.SetPaused -> onSetPaused(action.value)
                }
            }
        }
    }
}
