package com.example.taalimisafar.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen // Import this!
import com.example.taalimisafar.ui.screens.SplashScreen

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()

    // Start at "splash_screen"
    NavHost(navController = navController, startDestination = "splash_screen") {

        // 1. SPLASH SCREEN (Logo)
        // Automatically goes to "quote_screen" after animation
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        // 2. QUOTE SCREEN (Language Selection)
        // User picks language here, then goes to "home_screen"
        composable("quote_screen") {
            QuoteScreen(navController = navController)
        }

        // 3. MAIN/HOME SCREEN
        // The main app content
        composable("home_screen") {
            MainScreen(
                rootNavController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
    }
}