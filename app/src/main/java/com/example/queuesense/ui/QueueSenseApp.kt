package com.example.queuesense.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.queuesense.ui.navigation.NavGraph
import com.example.queuesense.ui.navigation.Screen
import com.example.queuesense.ui.theme.QueueSenseTheme
import com.example.queuesense.viewmodel.QueueViewModel

@Composable
fun QueueSenseApp(viewModel: QueueViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    QueueSenseTheme {
        Scaffold(
            bottomBar = {
                // Only show bottom bar on top-level screens
                val topLevelScreens = listOf(Screen.Home.route, Screen.Forecast.route, Screen.Profile.route)
                if (currentDestination in topLevelScreens) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentDestination == Screen.Home.route,
                            onClick = { 
                                if (currentDestination != Screen.Home.route) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentDestination == Screen.Forecast.route,
                            onClick = {
                                if (currentDestination != Screen.Forecast.route) {
                                    navController.navigate(Screen.Forecast.route) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                            label = { Text("Map") }
                        )
                        NavigationBarItem(
                            selected = currentDestination == Screen.Profile.route,
                            onClick = {
                                if (currentDestination != Screen.Profile.route) {
                                    navController.navigate(Screen.Profile.route) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") }
                        )
                    }
                }
            }
        ) { padding ->
            Surface(modifier = Modifier.padding(padding)) {
                NavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
