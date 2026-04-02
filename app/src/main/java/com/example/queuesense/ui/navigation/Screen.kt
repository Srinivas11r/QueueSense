package com.example.queuesense.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Forecast : Screen("forecast")
    object Details : Screen("details/{locationId}") {
        fun createRoute(locationId: String) = "details/$locationId"
    }
    object Report : Screen("report/{locationId}") {
        fun createRoute(locationId: String) = "report/$locationId"
    }
    object Cinema : Screen("cinema")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
