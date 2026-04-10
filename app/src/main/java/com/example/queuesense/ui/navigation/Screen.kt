package com.example.queuesense.ui.navigation

sealed class Screen(val route: String) {
    // Auth Screens
    object Login : Screen("login")
    object Signup : Screen("signup")
    
    // Main Screens
    object Home : Screen("home")
    object Forecast : Screen("forecast")
    object Profile : Screen("profile")
    
    // Detail Screens
    object Details : Screen("details/{locationId}") {
        fun createRoute(locationId: String) = "details/$locationId"
    }
    object Report : Screen("report/{locationId}") {
        fun createRoute(locationId: String) = "report/$locationId"
    }
    
    // Additional Profile Screens
    object EditProfile : Screen("edit_profile")
    object Notifications : Screen("notifications")
    object History : Screen("history")
    object Help : Screen("help")
}
