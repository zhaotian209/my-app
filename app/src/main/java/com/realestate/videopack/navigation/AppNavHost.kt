package com.realestate.videopack.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.realestate.videopack.presentation.ui.form.HouseFormScreen
import com.realestate.videopack.presentation.ui.form.HouseFormViewModel
import com.realestate.videopack.presentation.ui.home.HomeScreen
import com.realestate.videopack.presentation.ui.preview.PreviewScreen
import com.realestate.videopack.presentation.ui.upload.UploadScreen
import com.realestate.videopack.presentation.ui.upload.UploadViewModel
import com.realestate.videopack.presentation.ui.batch.BatchScreen
import com.realestate.videopack.presentation.ui.settings.SettingsScreen
import com.realestate.videopack.presentation.videolibrary.VideoLibraryScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val screens = listOf(Screen.Home, Screen.VideoLibrary, Screen.Batch, Screen.Settings)
                screens.forEach { screen ->
                    if (screen.icon != null) {
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
        ) {
            addHomeScreen(navController)
            addVideoLibraryScreen()
            addBatchScreen(navController)
            addSettingsScreen(navController)
            addUploadScreen(navController)
            addHouseFormScreen(navController)
            addPreviewScreen(navController)
        }
    }
}

fun NavGraphBuilder.addHomeScreen(navController: NavHostController) {
    composable(Screen.Home.route) {
        HomeScreen(navController = navController)
    }
}

fun NavGraphBuilder.addVideoLibraryScreen() {
    composable(Screen.VideoLibrary.route) {
        VideoLibraryScreen()
    }
}

fun NavGraphBuilder.addUploadScreen(navController: NavHostController) {
    composable(Screen.Upload.route) {
        val viewModel: UploadViewModel = hiltViewModel()
        UploadScreen(navController = navController, viewModel = viewModel)
    }
}

fun NavGraphBuilder.addHouseFormScreen(navController: NavHostController) {
    composable(Screen.HouseForm.route) {
        val viewModel: HouseFormViewModel = hiltViewModel()
        HouseFormScreen(navController = navController, viewModel = viewModel)
    }
}

fun NavGraphBuilder.addBatchScreen(navController: NavHostController) {
    composable(Screen.Batch.route) {
        BatchScreen(navController = navController)
    }
}

fun NavGraphBuilder.addSettingsScreen(navController: NavHostController) {
    composable(Screen.Settings.route) {
        SettingsScreen(navController = navController)
    }
}

fun NavGraphBuilder.addPreviewScreen(navController: NavHostController) {
    composable(Screen.Preview.route) {
        PreviewScreen(navController = navController)
    }
}
