package com.example.taalimisafar.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen
import com.example.taalimisafar.ui.screens.SplashScreen
import com.example.taalimisafar.ui.scholarships.ScholarshipScreen // ✅ 1. IMPORT THIS

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()

    // Start at "splash_screen"
    NavHost(navController = navController, startDestination = "splash_screen") {

        // 1. SPLASH SCREEN
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        // 2. QUOTE SCREEN
        composable("quote_screen") {
            QuoteScreen(navController = navController)
        }

        // 3. MAIN SCREEN
        composable("home_screen") {
            MainScreen(
                rootNavController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        // ✅ 4. ADD THIS: SCHOLARSHIP ROUTE
        composable("scholarship_tab") {
            ScholarshipScreen(navController = navController)
        }
    }
}