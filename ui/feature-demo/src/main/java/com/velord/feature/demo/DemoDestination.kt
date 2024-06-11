package com.velord.feature.demo

enum class DemoDest {
    Shape,
    Modifier,
    FlowSummator,
    Morph,
    HintPhoneNumber
}

interface DemoNavigator {
    fun goTo(dest: DemoDest)
}