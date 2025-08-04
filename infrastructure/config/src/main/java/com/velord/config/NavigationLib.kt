package com.velord.config

enum class NavigationLib {
    Voyager,
    Jetpack,
    Destinations,
    Compose;

    val isJetpack: Boolean get() = this == Jetpack
}