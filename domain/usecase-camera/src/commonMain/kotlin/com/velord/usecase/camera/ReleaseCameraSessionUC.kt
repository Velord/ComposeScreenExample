package com.velord.usecase.camera

fun interface ReleaseCameraSessionUC {
    suspend operator fun invoke()
}
