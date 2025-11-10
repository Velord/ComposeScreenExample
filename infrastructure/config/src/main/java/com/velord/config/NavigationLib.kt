package com.velord.config

enum class NavigationLib {
    Voyager,
    Jetpack,
    Destinations,
    Compose,
    Nav3;

    val isJetpack: Boolean get() = this == Jetpack
}