package com.velord.feature.demo

enum class DemoDestinationNavigationEvent {
    Shape,
    Modifier,
    FlowSummator,
    Morph,
    HintPhoneNumber,
    Movie,
    Dialog,
}

interface DemoNavigator {
    fun goTo(dest: DemoDestinationNavigationEvent)
}