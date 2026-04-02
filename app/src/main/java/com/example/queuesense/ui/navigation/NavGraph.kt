package com.example.queuesense.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.queuesense.ui.screens.details.LocationDetailsScreen
import com.example.queuesense.ui.screens.forecast.ForecastScreen
import com.example.queuesense.ui.screens.home.HomeScreen
import com.example.queuesense.ui.screens.profile.ProfileScreen
import com.example.queuesense.ui.screens.report.ReportScreen
import com.example.queuesense.viewmodel.QueueViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: QueueViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
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
            ProfileScreen(viewModel = viewModel)
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
