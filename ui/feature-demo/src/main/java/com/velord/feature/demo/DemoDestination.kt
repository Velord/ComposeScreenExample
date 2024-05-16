package com.velord.feature.demo

enum class DemoDest {
    Shape,
    Modifier,
    FlowSummator,
    Morph
}

interface DemoNavigator {
    fun goTo(dest: DemoDest)
}