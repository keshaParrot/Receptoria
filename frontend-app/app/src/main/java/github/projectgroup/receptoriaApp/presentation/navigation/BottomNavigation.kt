package github.projectgroup.receptoriaApp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.projectgroup.receptoriaApp.ui.theme.Orange500

enum class BottomNavItem(
    val route: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
    val label: String
) {
    HOME(
        route = "feed",
        selectedIcon = { Icon(Icons.Filled.Home, contentDescription = "Головна") },
        unselectedIcon = { Icon(Icons.Outlined.Home, contentDescription = "Головна") },
        label = "Головна"
    ),
    SEARCH(
        route = "search",
        selectedIcon = { Icon(Icons.Filled.Search, contentDescription = "Пошук") },
        unselectedIcon = { Icon(Icons.Outlined.Search, contentDescription = "Пошук") },
        label = "Пошук"
    ),
    PROFILE(
        route = "profile",
        selectedIcon = { Icon(Icons.Filled.Person, contentDescription = "Профіль") },
        unselectedIcon = { Icon(Icons.Outlined.Person, contentDescription = "Профіль") },
        label = "Профіль"
    )
}

@Composable
fun ReceptoriaBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    if (currentRoute == item.route) {
                        item.selectedIcon()
                    } else {
                        item.unselectedIcon()
                    }
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Orange500,
                    selectedTextColor = Orange500,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
