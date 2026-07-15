package com.velord.usecase.camera

fun interface CreateCameraSessionUC {
    suspend operator fun invoke()
}
