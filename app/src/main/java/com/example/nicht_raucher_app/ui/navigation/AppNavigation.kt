package com.example.nicht_raucher_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nicht_raucher_app.ui.add_habit.AddHabitScreen
import com.example.nicht_raucher_app.ui.dashboard.DashboardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onAddHabit = { navController.navigate("add_habit") }
            )
        }
        composable("add_habit") {
            AddHabitScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}