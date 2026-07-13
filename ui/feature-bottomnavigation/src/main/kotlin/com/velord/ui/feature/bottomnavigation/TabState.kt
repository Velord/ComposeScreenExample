package com.velord.ui.feature.bottomnavigation

enum class BottomNavigationItem {
    Camera,
    Demo,
    Setting;
}

data class TabState(
    val previous: BottomNavigationItem,
    val current: BottomNavigationItem
) {
    val isSame: Boolean get() = previous == current

    companion object {
        val DEFAULT = TabState(
            previous = BottomNavigationItem.Demo,
            current = BottomNavigationItem.Demo
        )
    }
}
