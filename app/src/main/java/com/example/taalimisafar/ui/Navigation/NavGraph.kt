package com.example.taalimisafar.ui.Navigation

import AboutUsScreen
import FeedbackScreen
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- REMOTE CLIENT IMPORT ---
import com.example.taalimisafar.data.remote.RetrofitClient
import com.example.taalimisafar.data.remote.ApiService

// --- CORE SCREENS ---
import com.example.taalimisafar.ui.screens.MainScreen
import com.example.taalimisafar.ui.screens.QuoteScreen
import com.example.taalimisafar.ui.screens.SimpleDetailScreen
import com.example.taalimisafar.ui.screens.SplashScreen
import com.example.taalimisafar.ui.screens.CategoryScreen
import com.example.taalimisafar.ui.screens.CommunityDetailScreen
import com.example.taalimisafar.ui.screens.CommunityScreen

// --- AUTH & PROFILE ---
import com.example.taalimisafar.ui.auth.LoginScreen
import com.example.taalimisafar.ui.auth.SignupScreen
import com.example.taalimisafar.ui.auth.ProfileScreen
import com.example.taalimisafar.ui.auth.EditProfileScreen

// --- SCHOLARSHIPS ---
import com.example.taalimisafar.ui.scholarships.DynamicCategoryScreen
import com.example.taalimisafar.ui.scholarships.DynamicTypeScreen
import com.example.taalimisafar.ui.scholarships.ScholarshipDetailScreen
import com.example.taalimisafar.ui.scholarships.ScholarshipScreen

// --- INTERNSHIPS ---
import com.example.taalimisafar.ui.internships.InternshipListScreen
import com.example.taalimisafar.ui.internships.InternshipDetailScreen

// --- JOBS ---
import com.example.taalimisafar.ui.jobs.JobDetailScreen
import com.example.taalimisafar.ui.jobs.JobListScreen

// --- SKILLS ---
import com.example.taalimisafar.ui.skills.SkillDetailScreen
import com.example.taalimisafar.ui.skills.SkillListScreen

// --- RELIGIOUS STUDIES ---
import com.example.taalimisafar.ui.religious.ReligiousDetailScreen
import com.example.taalimisafar.ui.religious.ReligiousListScreen

// --- GOVT SCHEMES ---
import com.example.taalimisafar.ui.schemes.SchemeListScreen
import com.example.taalimisafar.ui.schemes.SchemeDetailScreen

// --- ACADEMIC ---
import com.example.taalimisafar.ui.academic.AcademicListScreen
import com.example.taalimisafar.ui.academic.AcademicDetailScreen

// --- DIPLOMAS ---
import com.example.taalimisafar.ui.diplomas.DiplomaListScreen
import com.example.taalimisafar.ui.diplomas.DiplomaDetailScreen

// --- CAREER & INDUSTRY ---
import com.example.taalimisafar.ui.CareerAndIndustry.IndustryListScreen
import com.example.taalimisafar.ui.CareerAndIndustry.IndustryDetailScreen

// --- WOMEN'S EMPOWERMENT ---
import com.example.taalimisafar.ui.women.WomenListScreen
import com.example.taalimisafar.ui.women.WomenDetailScreen

// --- IMPORTANT DATES ---
import com.example.taalimisafar.ui.important_dates.ImportantListScreen
import com.example.taalimisafar.ui.important_dates.ImportantDetailScreen

// --- VIEW MODELS ---
import com.example.taalimisafar.viewmodel.InternshipViewModel
import com.example.taalimisafar.viewmodel.JobViewModel
import com.example.taalimisafar.viewmodel.ScholarshipViewModel
import com.example.taalimisafar.viewmodel.SkillViewModel
import com.example.taalimisafar.viewmodel.ReligiousViewModel
import com.example.taalimisafar.viewmodel.DiplomaViewModel
import com.example.taalimisafar.viewmodel.AcademicViewModel
import com.example.taalimisafar.viewmodel.SchemeViewModel
import com.example.taalimisafar.viewmodel.IndustryViewModel
import com.example.taalimisafar.viewmodel.WomenViewModel
import com.example.taalimisafar.viewmodel.ImportantDatesViewModel
import com.example.taalimisafar.viewmodel.AuthViewModel
import com.example.taalimisafar.viewmodel.CommunityViewModel
import com.example.taalimisafar.viewmodel.EducationViewModel
import com.example.taalimisafar.viewmodel.CoreViewModel

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Get the secure API Service
    val apiService = RetrofitClient.getClient(context.applicationContext)

    // 2. 🔥 THE MAGIC FACTORY 🔥
    val sharedFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return try {
                modelClass.getConstructor(ApiService::class.java).newInstance(apiService)
            } catch (e: NoSuchMethodException) {
                try {
                    modelClass.getConstructor(android.content.Context::class.java).newInstance(context.applicationContext)
                } catch (e2: NoSuchMethodException) {
                    modelClass.getConstructor().newInstance()
                }
            } as T
        }
    }

    // 3. APPLY THE FACTORY TO ALL VIEWMODELS
    val scholarshipViewModel: ScholarshipViewModel = viewModel(factory = sharedFactory)
    val internshipViewModel: InternshipViewModel = viewModel(factory = sharedFactory)
    val jobViewModel: JobViewModel = viewModel(factory = sharedFactory)
    val skillViewModel: SkillViewModel = viewModel(factory = sharedFactory)
    val religiousViewModel: ReligiousViewModel = viewModel(factory = sharedFactory)
    val diplomaViewModel: DiplomaViewModel = viewModel(factory = sharedFactory)
    val academicViewModel: AcademicViewModel = viewModel(factory = sharedFactory)
    val schemeViewModel: SchemeViewModel = viewModel(factory = sharedFactory)
    val industryViewModel: IndustryViewModel = viewModel(factory = sharedFactory)
    val womenViewModel: WomenViewModel = viewModel(factory = sharedFactory)
    val importantViewModel: ImportantDatesViewModel = viewModel(factory = sharedFactory)
    val authViewModel: AuthViewModel = viewModel(factory = sharedFactory)
    val communityViewModel: CommunityViewModel = viewModel(factory = sharedFactory)
    val educationViewModel: EducationViewModel = viewModel(factory = sharedFactory)

    // 🔥 ADDED THIS LINE: Creates your com.example.taalimisafar.viewmodel.CoreViewModel so the NavGraph knows what it is!
    val coreViewModel: CoreViewModel = viewModel(factory = sharedFactory)

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
                onThemeToggle = onThemeToggle,
                authViewModel = authViewModel,
                communityViewModel = communityViewModel,
                educationViewModel = educationViewModel
            )
        }

        // ==========================================
        // AUTHENTICATION & PROFILE ROUTES
        // ==========================================
        composable("login") {
            LaunchedEffect(authViewModel.isAuthenticated.value) {
                if (authViewModel.isAuthenticated.value) {
                    navController.navigate("profile") { popUpTo("login") { inclusive = true } }
                }
            }
            LoginScreen(
                viewModel = authViewModel,
                navController = navController,
                onNavigateToSignup = { navController.navigate("signup") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("signup") {
            LaunchedEffect(authViewModel.isAuthenticated.value) {
                if (authViewModel.isAuthenticated.value) {
                    navController.navigate("profile") { popUpTo("signup") { inclusive = true } }
                }
            }
            SignupScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("profile") {
            LaunchedEffect(authViewModel.isAuthenticated.value) {
                if (!authViewModel.isAuthenticated.value) {
                    navController.navigate("login") { popUpTo("profile") { inclusive = true } }
                }
            }

            ProfileScreen(
                viewModel = authViewModel,
                navController = navController,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home_screen") { inclusive = false }
                    }
                },
                onAboutUsClick = { navController.navigate("about_us") },
                onFeedbackClick = { navController.navigate("feedback") }
            )
        }

        composable("about_us") {
            AboutUsScreen(
                viewModel = coreViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("feedback") {
            FeedbackScreen(
                viewModel = coreViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // ==========================================
        // COMMUNITY / SOCIETY ROUTES
        // ==========================================

        composable("community") {
            CommunityScreen(
                navController = navController,
                viewModel = communityViewModel,
                authViewModel = authViewModel
            )
        }

        composable(
            route = "communityDetail/{questionId}",
            arguments = listOf(navArgument("questionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getInt("questionId") ?: 0
            CommunityDetailScreen(
                questionId = questionId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate("login") },
                viewModel = communityViewModel,
                authViewModel = authViewModel
            )
        }

        // ==========================================
        // SCHOLARSHIP ROUTES
        // ==========================================
        composable("scholarship_tab") { DynamicCategoryScreen(navController = navController) }
        composable(
            route = "scholarship_type/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType })
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
                navArgument("pageTitle") { type = NavType.StringType })
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

        // ==========================================
        // STATIC CATEGORY ROUTES (The Master Grid)
        // ==========================================
        composable("academic") { CategoryScreen(navController, "Academic", "academic") }
        composable("diploma") { CategoryScreen(navController, "Diploma", "diploma") }
        composable("internships") { CategoryScreen(navController, "Internships", "internships") }
        composable("skills") { CategoryScreen(navController, "Skill Development", "skills") }
        composable("important_dates") { CategoryScreen(navController, "Important Dates", "important_dates") }
        composable("govt_jobs") { CategoryScreen(navController, "Govt Jobs", "govt_jobs") }
        composable("private_jobs") { CategoryScreen(navController, "Private Jobs", "private_jobs") }
        composable("sports") { CategoryScreen(navController, "Career Industry", "sports") }
        composable("hobbies") { CategoryScreen(navController, "Religious Studies", "hobbies") }
        composable("govt_schemes") { CategoryScreen(navController, "Govt Schemes", "govt_schemes") }
        composable("women") { CategoryScreen(navController, "Women Empowerment", "women") }

        // ==========================================
        // SIMPLE DETAIL FALLBACK ROUTES
        // ==========================================
        composable(
            route = "category_detail/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            SimpleDetailScreen(
                navController = navController,
                title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            )
        }
        composable(
            route = "career_industry_detail/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            SimpleDetailScreen(
                navController = navController,
                title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            )
        }
        composable(
            route = "religious_studies_detail/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            SimpleDetailScreen(
                navController = navController,
                title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            )
        }
        composable(
            route = "education_board_detail/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            SimpleDetailScreen(
                navController = navController,
                title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            )
        }

        // ==========================================
        // GOVT SCHEMES ROUTES
        // ==========================================
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
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = schemeViewModel,
                onBackClick = { navController.popBackStack() },
                onSchemeClick = { schemeId ->
                    navController.navigate("scheme_detail/$schemeId")
                }
            )
        }
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

        // ==========================================
        // INTERNSHIP ROUTES
        // ==========================================
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

        // ==========================================
        // JOB ROUTES
        // ==========================================
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

        // ==========================================
        // SKILL DEVELOPMENT ROUTES
        // ==========================================
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

        // ==========================================
        // RELIGIOUS STUDIES ROUTES
        // ==========================================
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

        // ==========================================
        // DIPLOMA ROUTES
        // ==========================================
        composable(
            route = "diploma_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Diplomas")

            DiplomaListScreen(
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

            DiplomaDetailScreen(
                programId = programId,
                viewModel = diplomaViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==========================================
        // ACADEMIC (STREAMS / COURSES) ROUTES
        // ==========================================
        composable(
            route = "academic_list/{streamId}/{streamName}",
            arguments = listOf(
                navArgument("streamId") { type = NavType.IntType },
                navArgument("streamName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val streamId = backStackEntry.arguments?.getInt("streamId") ?: 0
            val streamName = Uri.decode(backStackEntry.arguments?.getString("streamName") ?: "Courses")

            AcademicListScreen(
                streamId = streamId,
                streamName = streamName,
                viewModel = academicViewModel,
                onBackClick = { navController.popBackStack() },
                onCourseClick = { courseId -> navController.navigate("academic_detail/$courseId") }
            )
        }
        composable(
            route = "academic_detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

            AcademicDetailScreen(
                courseId = courseId,
                viewModel = academicViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==========================================
        // CAREER & INDUSTRY ROUTES
        // ==========================================
        composable(
            route = "industry_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Careers")

            IndustryListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = industryViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { programId ->
                    navController.navigate("industry_detail/$programId")
                }
            )
        }
        composable(
            route = "industry_detail/{programId}",
            arguments = listOf(navArgument("programId") { type = NavType.IntType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            LaunchedEffect(programId) {
                industryViewModel.fetchProgramDetail(programId)
            }
            val selectedProgram by industryViewModel.selectedProgram.collectAsState()

            IndustryDetailScreen(
                program = selectedProgram,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==========================================
        // WOMEN'S EMPOWERMENT ROUTES
        // ==========================================
        composable(
            route = "women_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "Programs")

            WomenListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = womenViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { id -> navController.navigate("women_detail/$id") }
            )
        }
        composable(
            route = "women_detail/{programId}",
            arguments = listOf(navArgument("programId") { type = NavType.IntType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            WomenDetailScreen(
                programId = programId,
                viewModel = womenViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==========================================
        // IMPORTANT DATES ROUTES
        // ==========================================
        composable(
            route = "important_list/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

            ImportantListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = importantViewModel,
                onBackClick = { navController.popBackStack() },
                onProgramClick = { programId ->
                    navController.navigate("important_detail/$programId")
                }
            )
        }
        composable(
            route = "important_detail/{programId}",
            arguments = listOf(
                navArgument("programId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getInt("programId") ?: 0

            ImportantDetailScreen(
                programId = programId,
                viewModel = importantViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}