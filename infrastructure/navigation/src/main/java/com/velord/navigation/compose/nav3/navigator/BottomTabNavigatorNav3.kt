package com.velord.navigation.compose.nav3.navigator

//interface BottomTabNavigatorNav3 {
//    fun getRouteOnTabClickNav3(route: BottomNavigationItem): Any
//    fun getTabStartRouteNav3(route: BottomNavigationItem): Any
//}
//
//internal fun onTabClickNav3(
//    isSelected: Boolean,
//    item: BottomNavigationItem,
//    backStack: SnapshotStateList<NavKey>,
//    navigator: BottomTabNavigatorNav3,
//) {
//    if (isSelected) {
//        // When we click again on a bottom bar item and it was already selected
//        // we want to pop the back stack until the initial destination of this bottom bar item
//        val start = navigator.getTabStartRouteNav3(item)
//        backStack.remove(start, false)
//        return
//    }
//
//    val destination = navigator.getRouteOnTabClickNav3(item)
//    navController.navigate(destination) {
//        // Pop up to the root of the graph to
//        // avoid building up a large stack of destinations
//        // on the back stack as users select items
//        popUpTo(navController.graph.findStartDestination().id) {
//            saveState = true
//        }
//        // Avoid multiple copies of the same destination when reselecting the same item
//        launchSingleTop = true
//        // Restore state when reselecting a previously selected item
//        restoreState = true
//    }
//}