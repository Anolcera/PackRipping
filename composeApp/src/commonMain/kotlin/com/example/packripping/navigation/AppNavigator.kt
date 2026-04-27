package com.example.packripping.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

class AppNavigator(val backStack: SnapshotStateList<Any>) {

    var isTopLevelSwitch: Boolean by mutableStateOf(false)
        private set

    val currentRoute: Any?
        get() = backStack.lastOrNull()

    fun navigate(route: Any) {
        isTopLevelSwitch = false
        backStack.add(route)
    }

    fun navigateToTopLevel(route: TopLevelDestination) {
        isTopLevelSwitch = true
        while (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
        backStack[0] = route.route
    }

    fun goBack(): Boolean {
        if (backStack.size <= 1) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }

    fun popTo(route: Any) {
        while (backStack.size > 1 && backStack.last() != route) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}
