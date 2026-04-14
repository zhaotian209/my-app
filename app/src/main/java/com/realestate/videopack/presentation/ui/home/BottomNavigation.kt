package com.realestate.videopack.presentation.ui.home

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.dp
import com.realestate.videopack.navigation.Screen
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.Surface

@Composable
fun BottomNavigation(navController: NavHostController) {
    NavigationBar(
        containerColor = Surface,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val screens = listOf(Screen.Home, Screen.Batch, Screen.Settings)
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    androidx.compose.material3.Icon(
                        imageVector = screen.icon!!,
                        contentDescription = screen.title,
                        tint = if (currentRoute == screen.route) Gold else Color.Gray
                    )
                },
                label = {
                    Text(
                        screen.title,
                        color = if (currentRoute == screen.route) Gold else Color.Gray
                    )
                }
            )
        }
    }
}
