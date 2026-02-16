package com.example.taalimisafar.ui.Navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// --- Imports ---
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen
import com.example.taalimisafar.ui.screens.SplashScreen
import com.example.taalimisafar.ui.scholarships.DynamicCategoryScreen
import com.example.taalimisafar.ui.scholarships.DynamicTypeScreen
import com.example.taalimisafar.ui.scholarships.ScholarshipDetailScreen
import com.example.taalimisafar.ui.scholarships.ScholarshipScreen
import com.example.taalimisafar.viewmodel.ScholarshipViewModel

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()

    // ✅ 1. Initialize the ViewModel here so it's shared across all screens
    val scholarshipViewModel: ScholarshipViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash_screen") {

        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        composable("quote_screen") {
            QuoteScreen(navController = navController)
        }

        composable("home_screen") {
            MainScreen(
                rootNavController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        // ==========================================
        //         SCHOLARSHIP 3-STEP FLOW
        // ==========================================

        composable("scholarship_tab") {
            DynamicCategoryScreen(navController = navController)
        }

        composable(
            route = "scholarship_type/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "")

            DynamicTypeScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName
            )
        }

        composable(
            route = "scholarship_list/{categoryId}/{typeId}/{pageTitle}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("typeId") { type = NavType.IntType },
                navArgument("pageTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val typeId = backStackEntry.arguments?.getInt("typeId") ?: 0
            val pageTitle = Uri.decode(backStackEntry.arguments?.getString("pageTitle") ?: "Scholarships")

            ScholarshipScreen(
                navController = navController,
                categoryId = categoryId,
                typeId = typeId,
                pageTitle = pageTitle,
                viewModel = scholarshipViewModel // ✅ Pass shared instance
            )
        }

        composable("scholarship_detail") {
            // ✅ Fix: Pass the 'scholarshipViewModel' variable, not the class name
            ScholarshipDetailScreen(
                navController = navController,
                viewModel = scholarshipViewModel
            )
        }
    }
}