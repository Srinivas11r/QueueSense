package com.example.queuesense.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.queuesense.ui.screens.auth.LoginScreen
import com.example.queuesense.ui.screens.auth.SignupScreen
import com.example.queuesense.ui.screens.details.LocationDetailsScreen
import com.example.queuesense.ui.screens.forecast.ForecastScreen
import com.example.queuesense.ui.screens.home.HomeScreen
import com.example.queuesense.ui.screens.profile.EditProfileScreen
import com.example.queuesense.ui.screens.profile.HistoryScreen
import com.example.queuesense.ui.screens.profile.ProfileScreen
import com.example.queuesense.ui.screens.report.ReportScreen
import com.example.queuesense.viewmodel.QueueViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: QueueViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Screen.Login.route else Screen.Home.route
    ) {
        // --- Auth Screens ---
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Signup.route) {
            SignupScreen(
                viewModel = viewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Main App Screens ---
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onLocationClick = { location ->
                    navController.navigate(Screen.Details.createRoute(location.id))
                },
                onReportClick = { location ->
                    navController.navigate(Screen.Report.createRoute(location.id))
                }
            )
        }
        
        composable(Screen.Forecast.route) {
            ForecastScreen(viewModel = viewModel)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToNotifications = { /* navController.navigate(Screen.Notifications.route) */ },
                onNavigateToHelp = { /* navController.navigate(Screen.Help.route) */ }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId") ?: ""
            LocationDetailsScreen(
                viewModel = viewModel,
                locationId = locationId,
                onBack = { navController.popBackStack() },
                onReportClick = {
                    navController.navigate(Screen.Report.createRoute(locationId))
                }
            )
        }
        
        composable(
            route = Screen.Report.route,
            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId") ?: ""
            ReportScreen(
                viewModel = viewModel,
                locationId = locationId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
