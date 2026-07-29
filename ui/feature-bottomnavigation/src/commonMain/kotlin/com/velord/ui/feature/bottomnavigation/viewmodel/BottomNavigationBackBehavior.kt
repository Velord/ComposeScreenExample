package com.velord.ui.feature.bottomnavigation.viewmodel

enum class BottomNavigationBackBehavior {
    DelegateToNavigator,
    ReturnToDefaultTab,
    ConfirmExit;

    val isConfirmExit: Boolean get() = this == ConfirmExit
}
