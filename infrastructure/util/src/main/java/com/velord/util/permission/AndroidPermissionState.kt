package com.velord.util.permission

enum class AndroidPermissionState {
    NotAsked,
    Granted,
    Denied,
    Rationale,
    // Not all time
    PossiblePermanentDeny;

    val isNotAsked get() = this == NotAsked
    val isGranted get() = this == Granted
    val isDenied get() = this == Denied
    val isRationale get() = this == Rationale
    val isForbidden get() = this == Denied || this == Rationale || this == PossiblePermanentDeny
    val isPossiblePermanentDeny get() = this == PossiblePermanentDeny

    companion object {
        fun invoke(value: Boolean) = when (value) {
            true -> Granted
            false -> Denied
        }
    }
}