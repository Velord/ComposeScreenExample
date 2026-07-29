package com.velord.model.camera.config

enum class CameraLens {
    Front,
    Back;

    val isFront: Boolean get() = this == Front
}