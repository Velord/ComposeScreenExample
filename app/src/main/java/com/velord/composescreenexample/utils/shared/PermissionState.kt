package com.velord.composescreenexample.utils.shared

enum class PermissionState {
    NotAsked,
    Granted,
    Denied;

    fun isGranted() = this == Granted
    fun isDenied() = this == Denied

    companion object {
        fun invoke(value: Boolean) = when (value) {
            true -> Granted
            false -> Denied
        }
    }
}