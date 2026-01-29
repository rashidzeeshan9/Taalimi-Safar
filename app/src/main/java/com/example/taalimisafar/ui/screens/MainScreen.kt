package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
// IMPORTS
import com.example.taalimisafar.ui.courses.CourseListScreen
// Make sure HomeScreen is imported (if it's in the same package, it might not need an import line, but check)
// import com.example.taalimisafar.ui.screens.HomeScreen

/* -------------------- BOTTOM NAV -------------------- */

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_tab", "Home", Icons.Default.Home)
    object Colleges : BottomNavItem("colleges_tab", "Colleges", Icons.Default.Search)
    object Exam : BottomNavItem("exam_tab", "Exam", Icons.Default.Edit)
    object Course : BottomNavItem("course_tab", "Courses", Icons.Default.PlayArrow)
    object Profile : BottomNavItem("profile_tab", "Profile", Icons.Default.Person)
}

/* -------------------- MAIN SCREEN -------------------- */

@Composable
fun MainScreen(
    rootNavController: NavController,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Colleges,
        BottomNavItem.Exam,
        BottomNavItem.Course,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            // 1. HOME TAB (UPDATED!)
            composable(BottomNavItem.Home.route) {
                // We call the real HomeScreen here now
                HomeScreen(
                    navController = rootNavController,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }

            // 2. COLLEGES TAB
            composable(BottomNavItem.Colleges.route) {
                PlaceholderScreen("Colleges & Search")
            }

            // 3. EXAMS TAB
            composable(BottomNavItem.Exam.route) {
                PlaceholderScreen("Upcoming Exam")
            }

            // 4. COURSE TAB
            composable(BottomNavItem.Course.route) {
                CourseListScreen()
            }

            // 5. PROFILE TAB
            composable(BottomNavItem.Profile.route) {
                PlaceholderScreen("User Profile")
            }
        }
    }
}

/* -------------------- PLACEHOLDER -------------------- */
@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Building $name")
    }
}