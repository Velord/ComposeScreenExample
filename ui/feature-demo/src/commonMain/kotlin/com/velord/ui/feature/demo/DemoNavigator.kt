package com.velord.ui.feature.demo

enum class DemoNavigationEvent {
    Shape,
    Modifier,
    FlowSummator,
    Morph,
    HintPhoneNumber,
    Movie,
    Dialog,
}

interface DemoNavigator {
    fun goTo(dest: DemoNavigationEvent)
    fun goBack()
}
