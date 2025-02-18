package com.velord.feature.demo

enum class DemoDestinationNavigationEvent {
    Shape,
    Modifier,
    FlowSummator,
    Morph,
    HintPhoneNumber,
    Movie
}

interface DemoNavigator {
    fun goTo(dest: DemoDestinationNavigationEvent)
}