package com.example.packripping.navigation

import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: Any,
    val label: String,
    val icon: ImageVector?,
) {
    PACKS(PacksHome, "Packs", null),
    PULLS(PullsTab, "Pulls", null),
    SHOWROOM(ShowroomTab, "Showroom", null),
}

fun Any?.toTopLevelDestination(): TopLevelDestination? = when (this) {
    PacksHome -> TopLevelDestination.PACKS
    PullsTab -> TopLevelDestination.PULLS
    ShowroomTab -> TopLevelDestination.SHOWROOM
    else -> null
}
