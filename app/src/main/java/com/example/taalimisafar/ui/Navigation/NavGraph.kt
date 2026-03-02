package com.example.taalimisafar.ui.Navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen
import com.example.taalimisafar.ui.screens.SimpleDetailScreen
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

    val scholarshipViewModel: ScholarshipViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {

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

        // Scholarship flow

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
                viewModel = scholarshipViewModel
            )
        }

        composable("scholarship_detail") {
            ScholarshipDetailScreen(
                navController = navController,
                viewModel = scholarshipViewModel
            )
        }

        // Category routes

        composable("academic") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Academic", "academic")
        }
        composable("diploma") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Diploma", "diploma")
        }
        composable("women") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Women Empowerment", "women")
        }
        composable("internships") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Internships", "internships")
        }
        composable("skills") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Skill Development", "skills")
        }
        composable("important_dates") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Important Dates", "important_dates")
        }
        composable("govt_jobs") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Govt Jobs", "govt_jobs")
        }
        composable("private_jobs") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Private Jobs", "private_jobs")
        }
        composable("govt_schemes") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Govt Schemes", "govt_schemes")
        }
        composable("sports") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Career Industry", "sports")
        }
        composable("hobbies") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Religious Studies", "hobbies")
        }

        // Generic detail routes used by grid sections

        composable(
            route = "category_detail/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            SimpleDetailScreen(
                navController = navController,
                title = title
            )
        }

        composable(
            route = "career_industry_detail/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            SimpleDetailScreen(
                navController = navController,
                title = title
            )
        }

        composable(
            route = "religious_studies_detail/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            SimpleDetailScreen(
                navController = navController,
                title = title
            )
        }

        composable(
            route = "education_board_detail/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            SimpleDetailScreen(
                navController = navController,
                title = title
            )
        }
    }
}

