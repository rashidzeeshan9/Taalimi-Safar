package com.example.taalimisafar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen
import com.example.taalimisafar.ui.screens.SplashScreen

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("quote_screen") {
            QuoteScreen(
                onTimeout = {
                    navController.navigate("main") {
                        popUpTo("quote_screen") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                rootNavController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
    }
}