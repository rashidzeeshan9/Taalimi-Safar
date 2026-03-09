package com.example.taalimisafar.ui.Navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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

import com.example.taalimisafar.ui.internships.InternshipListScreen
import com.example.taalimisafar.ui.internships.InternshipDetailScreen
import com.example.taalimisafar.ui.jobs.JobDetailScreen
import com.example.taalimisafar.ui.jobs.JobListScreen
import com.example.taalimisafar.ui.skills.SkillDetailScreen
import com.example.taalimisafar.ui.skills.SkillListScreen
import com.example.taalimisafar.ui.religious.ReligiousDetailScreen
import com.example.taalimisafar.ui.religious.ReligiousListScreen

// --- NEW SCHEME IMPORTS ---
import com.example.taalimisafar.ui.schemes.SchemeListScreen
import com.example.taalimisafar.ui.schemes.SchemeDetailScreen

import com.example.taalimisafar.viewmodel.InternshipViewModel
import com.example.taalimisafar.viewmodel.JobViewModel
import com.example.taalimisafar.viewmodel.ScholarshipViewModel
import com.example.taalimisafar.viewmodel.SkillViewModel
import com.example.taalimisafar.viewmodel.ReligiousViewModel
import com.example.taalimisafar.viewmodel.DiplomaViewModel
import com.example.taalimisafar.viewmodel.SchemeViewModel // NEW

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ViewModels
    val scholarshipViewModel: ScholarshipViewModel = viewModel()
    val internshipViewModel: InternshipViewModel = viewModel()
    val jobViewModel: JobViewModel = viewModel()
    val skillViewModel: SkillViewModel = viewModel()
    val religiousViewModel: ReligiousViewModel = viewModel()
    val diplomaViewModel: DiplomaViewModel = viewModel()
    val schemeViewModel: SchemeViewModel = viewModel() // NEW

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") { SplashScreen(navController = navController) }
        composable("quote_screen") { QuoteScreen(navController = navController) }
        composable("home_screen") {
            MainScreen(
                rootNavController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        // --- SCHOLARSHIP ROUTES ---
        composable("scholarship_tab") { DynamicCategoryScreen(navController = navController) }
        composable(
            route = "scholarship_type/{categoryId}/{categoryName}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType }, navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "")
            DynamicTypeScreen(navController = navController, categoryId = categoryId, categoryName = categoryName)
        }
        composable(
            route = "scholarship_list/{categoryId}/{typeId}/{pageTitle}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType }, navArgument("typeId") { type = NavType.IntType }, navArgument("pageTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val typeId = backStackEntry.arguments?.getInt("typeId") ?: 0
            val pageTitle = Uri.decode(backStackEntry.arguments?.getString("pageTitle") ?: "Scholarships")
            ScholarshipScreen(navController = navController, categoryId = categoryId, typeId = typeId, pageTitle = pageTitle, viewModel = scholarshipViewModel)
        }
        composable("scholarship_detail") { ScholarshipDetailScreen(navController = navController, viewModel = scholarshipViewModel) }

        // --- STATIC CATEGORY ROUTES ---
        composable("academic") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Academic", "academic") }
        composable("diploma") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Diploma", "diploma") }
        composable("women") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Women Empowerment", "women") }
        composable("internships") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Internships", "internships") }
        composable("skills") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Skill Development", "skills") }
        composable("important_dates") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Important Dates", "important_dates") }
        composable("govt_jobs") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Govt Jobs", "govt_jobs") }
        composable("private_jobs") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Private Jobs", "private_jobs") }
        composable("sports") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Career Industry", "sports") }
        composable("hobbies") { com.example.taalimisafar.ui.screens.CategoryScreen(navController, "Religious Studies", "hobbies") }

// --- GOVT SCHEMES ROUTES (NEW) ---

        // 1. Home screen tile clicks here -> Opens the Category Grid!
        composable("govt_schemes") {
            com.example.taalimisafar.ui.screens.CategoryScreen(
                navController = navController,
                categoryTitle = "Govt Schemes",
                categoryId = "govt_schemes"
            )
        }

        // 2. Grid tile clicks here -> Opens the List Screen with the correct Category!
        composable(
            route = "scheme_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Schemes")

            SchemeListScreen(
                categoryId = categoryId,             // Now it has the required ID!
                categoryName = categoryName,         // Now it has the required Name!
                viewModel = schemeViewModel,
                onBackClick = { navController.popBackStack() },
                onSchemeClick = { schemeId ->
                    navController.navigate("scheme_detail/$schemeId")
                }
            )
        }

        // 3. List item clicks here -> Opens the Detail Screen!
        composable(
            route = "scheme_detail/{schemeId}",
            arguments = listOf(navArgument("schemeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val schemeId = backStackEntry.arguments?.getInt("schemeId") ?: 0

            LaunchedEffect(schemeId) {
                schemeViewModel.fetchSchemeDetail(schemeId)
            }

            val selectedScheme by schemeViewModel.selectedScheme.collectAsState()

            SchemeDetailScreen(
                scheme = selectedScheme,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- INTERNSHIP ROUTES ---
        composable(
            route = "internship_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Internships")

            LaunchedEffect(categoryId) {
                internshipViewModel.fetchInternships(categoryId)
            }

            InternshipListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = internshipViewModel,
                onBackClick = { navController.popBackStack() },
                onInternshipClick = { internshipId -> navController.navigate("internship_detail/$internshipId") }
            )
        }

        composable(
            route = "internship_detail/{internshipId}",
            arguments = listOf(navArgument("internshipId") { type = NavType.IntType })
        ) { backStackEntry ->
            val internshipId = backStackEntry.arguments?.getInt("internshipId") ?: 0

            InternshipDetailScreen(
                internshipId = internshipId,
                viewModel = internshipViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = "category_detail/{title}", arguments = listOf(navArgument("title") { type = NavType.StringType })) { backStackEntry -> SimpleDetailScreen(navController = navController, title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")) }
        composable(route = "career_industry_detail/{title}", arguments = listOf(navArgument("title") { type = NavType.StringType })) { backStackEntry -> SimpleDetailScreen(navController = navController, title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")) }
        composable(route = "religious_studies_detail/{title}", arguments = listOf(navArgument("title") { type = NavType.StringType })) { backStackEntry -> SimpleDetailScreen(navController = navController, title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")) }
        composable(route = "education_board_detail/{title}", arguments = listOf(navArgument("title") { type = NavType.StringType })) { backStackEntry -> SimpleDetailScreen(navController = navController, title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")) }

        // --- JOB ROUTES ---
        composable(
            route = "jobList/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

            JobListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = jobViewModel,
                onBackClick = { navController.popBackStack() },
                onJobClick = { job ->
                    navController.navigate("jobDetail/${job.id}")
                }
            )
        }

        composable(
            route = "jobDetail/{jobId}",
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 0

            JobDetailScreen(
                jobId = jobId,
                viewModel = jobViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- SKILL DEVELOPMENT ROUTES ---
        composable(
            route = "skill_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Skills")

            SkillListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = skillViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { programId ->
                    navController.navigate("skill_detail/$programId")
                }
            )
        }

        composable(
            route = "skill_detail/{programId}",
            arguments = listOf(navArgument("programId") { type = NavType.IntType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            SkillDetailScreen(
                programId = programId,
                viewModel = skillViewModel,
                onBackClick = { navController.popBackStack() },
                onEnrollClick = { url ->
                    if (url.isNotBlank()) {
                        val fullUrl = if (!url.startsWith("http")) "https://$url" else url
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl)))
                    }
                }
            )
        }

        // --- RELIGIOUS STUDIES ROUTES ---
        composable(
            route = "religious_list/{religionId}/{religionName}",
            arguments = listOf(
                navArgument("religionId") { type = NavType.IntType },
                navArgument("religionName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val religionId = backStackEntry.arguments?.getInt("religionId") ?: 0
            val religionName = Uri.decode(backStackEntry.arguments?.getString("religionName") ?: "Programs")

            ReligiousListScreen(
                religionId = religionId,
                religionName = religionName,
                viewModel = religiousViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { programId ->
                    navController.navigate("religious_detail/$programId")
                }
            )
        }

        composable(
            route = "religious_detail/{programId}",
            arguments = listOf(navArgument("programId") { type = NavType.IntType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            ReligiousDetailScreen(
                programId = programId,
                viewModel = religiousViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- DIPLOMA ROUTES ---
        composable(
            route = "diploma_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Diplomas")

            com.example.taalimisafar.ui.diplomas.DiplomaListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = diplomaViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { programId ->
                    navController.navigate("diploma_detail/$programId")
                }
            )
        }

        composable(
            route = "diploma_detail/{programId}",
            arguments = listOf(navArgument("programId") { type = NavType.IntType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            com.example.taalimisafar.ui.diplomas.DiplomaDetailScreen(
                programId = programId,
                viewModel = diplomaViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}