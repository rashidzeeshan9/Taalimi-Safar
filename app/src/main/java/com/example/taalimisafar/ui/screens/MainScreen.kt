package com.example.taalimisafar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// 🔥 IMPORTS
import com.example.taalimisafar.ui.courses.CourseListScreen
import com.example.taalimisafar.ui.education.EducationScreen // We will create this next
import com.example.taalimisafar.viewmodel.AuthViewModel
import com.example.taalimisafar.viewmodel.CommunityViewModel
import com.example.taalimisafar.viewmodel.EducationViewModel // Make sure this exists

/* -------------------- BOTTOM NAV -------------------- */
sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_tab", "Home", Icons.Default.Home)
    object Colleges : BottomNavItem("colleges_tab", "Education/\nSchool", Icons.Default.School)
    object Community : BottomNavItem("community_tab", "Society", Icons.Default.People)
    object Course : BottomNavItem("course_tab", "Courses", Icons.Default.PlayArrow)
    object Profile : BottomNavItem("profile_tab", "Profile", Icons.Default.Person)
}

/* -------------------- MAIN SCREEN -------------------- */
@Composable
fun MainScreen(
    rootNavController: NavController,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    authViewModel: AuthViewModel,
    communityViewModel: CommunityViewModel,
    educationViewModel: EducationViewModel // 🔥 Added this to pass data to the education screen!
) {
    val bottomNavController = rememberNavController()
    val context = LocalContext.current

    val items = listOf(
        BottomNavItem.Home, BottomNavItem.Colleges, BottomNavItem.Community,
        BottomNavItem.Course, BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 12.sp) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (item == BottomNavItem.Course) {
                                Toast.makeText(context, "Courses: Coming Soon", Toast.LENGTH_SHORT).show()
                            } else {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = rootNavController, isDarkTheme = isDarkTheme, onThemeToggle = onThemeToggle)
            }

            // 🔥 Uses the NEW Dynamic Education Screen
            composable(BottomNavItem.Colleges.route) {
                EducationScreen(navController = rootNavController)
            }

            composable(BottomNavItem.Community.route) {
                CommunityScreen(isDarkTheme = isDarkTheme, onThemeToggle = onThemeToggle, navController = rootNavController, viewModel = communityViewModel, authViewModel = authViewModel)
            }

            composable(BottomNavItem.Course.route) { CourseListScreen() }

            composable(BottomNavItem.Profile.route) {
                val isAuthenticated by authViewModel.isAuthenticated
                if (isAuthenticated) {
                    com.example.taalimisafar.ui.auth.ProfileScreen(
                        viewModel = authViewModel,
                        navController = rootNavController,
                        onNavigateToLogin = { authViewModel.logout() },
                        // 🔥 THE TWO LINES THAT FIX THE RED ERRORS:
                        onAboutUsClick = { rootNavController.navigate("about_us") },
                        onFeedbackClick = { rootNavController.navigate("feedback") }
                    )
                } else {
                    com.example.taalimisafar.ui.auth.LoginScreen(
                        viewModel = authViewModel,
                        navController = rootNavController,
                        onNavigateToSignup = { rootNavController.navigate("signup") },
                        onNavigateBack = {
                            bottomNavController.navigate(BottomNavItem.Home.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id); launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}