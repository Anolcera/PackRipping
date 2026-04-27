package com.example.packripping.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.packripping.core.ui.theme.PackRippingTheme

@Composable
fun AppNavigationBar(
    selected: TopLevelDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            val isSelected = destination == selected
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                icon = {
                    val tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                    val iconVector = destination.icon
                    if (iconVector != null) {
                        Icon(imageVector = iconVector, contentDescription = destination.label, tint = tint)
                    } else {
                        Box(Modifier.size(8.dp).background(tint, CircleShape))
                    }
                },
                label = { Text(destination.label) },
            )
        }
    }
}

@Preview
@Composable
private fun AppNavigationBarPreview() {
    PackRippingTheme {
        AppNavigationBar(
            selected = TopLevelDestination.PACKS,
            onNavigate = {},
        )
    }
}
