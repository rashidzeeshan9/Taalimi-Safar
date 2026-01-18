package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.screens.home.HomeScreen

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_tab", "Home", Icons.Default.Home)
    object Colleges : BottomNavItem("colleges_tab", "Colleges", Icons.Default.Search)
    object Exam : BottomNavItem("exam_tab" , "Exam", Icons.Default.Edit)
    object Course : BottomNavItem("course_tab", "Courses", Icons.Default.PlayArrow)
    object Profile : BottomNavItem("profile_tab", "Profile", Icons.Default.Person)
}

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
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
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
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    navController = rootNavController,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }
            composable(BottomNavItem.Colleges.route) { PlaceholderScreen("Colleges & Search") }
            composable(BottomNavItem.Course.route) { PlaceholderScreen("Educational Reels") }
            composable(BottomNavItem.Exam.route ){ PlaceholderScreen("Upcoming Exam") }
            composable(BottomNavItem.Profile.route) { PlaceholderScreen("User Profile") }
        }
    }
}
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Building $name")
    }
}