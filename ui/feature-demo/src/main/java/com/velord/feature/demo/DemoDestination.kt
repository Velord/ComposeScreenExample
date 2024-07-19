package com.velord.feature.demo

enum class DemoDest {
    Shape,
    Modifier,
    FlowSummator,
    Morph,
    HintPhoneNumber,
    Movie
}

interface DemoNavigator {
    fun goTo(dest: DemoDest)
}