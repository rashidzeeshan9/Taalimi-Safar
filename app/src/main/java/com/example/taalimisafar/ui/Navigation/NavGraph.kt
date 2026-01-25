package com.example.taalimisafar.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.SplashScreen

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    // When user clicks a language button, go to Main Screen
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true } // Remove Splash from back button history
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