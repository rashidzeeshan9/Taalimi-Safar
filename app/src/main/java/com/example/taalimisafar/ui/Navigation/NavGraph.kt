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

        // ✅ 4. SCHOLARSHIP ROUTE
        composable("scholarship_tab") {
            ScholarshipScreen(navController = navController)
        }

        // ✅ 5. OTHER CATEGORY ROUTES
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
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Sports Career", "sports")
        }
        composable("hobbies") {
            com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Good Hobbies", "hobbies")
        }
    }
}