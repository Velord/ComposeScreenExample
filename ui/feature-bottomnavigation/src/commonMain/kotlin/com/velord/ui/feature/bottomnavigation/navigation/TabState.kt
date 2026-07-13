package com.velord.ui.feature.bottomnavigation.navigation

data class TabState(
    val previous: BottomNavigationItem,
    val current: BottomNavigationItem,
) {
    val isSame: Boolean get() = previous == current

    companion object {
        val DEFAULT = TabState(
            previous = BottomNavigationItem.Demo,
            current = BottomNavigationItem.Demo
        )
    }
}
