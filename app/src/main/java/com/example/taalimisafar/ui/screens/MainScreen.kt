package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

/* -------------------- DATA MODEL -------------------- */

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val eligibility: String,
    val duration: String,
    val averageFees: String,
    val futureScope: String
)

/* -------------------- SAMPLE DATA -------------------- */

val sampleCourse = Course(
    id = 1,
    title = "Bachelor of Computer Applications (BCA)",
    description = "BCA is a 3-year undergraduate program focusing on computer applications and software development.",
    eligibility = "10+2 with Mathematics as a subject.",
    duration = "3 Years",
    averageFees = "₹50,000 – ₹1,50,000 per year",
    futureScope = "Software Developer, Data Analyst, MCA, Government Jobs"
)

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
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    navController = rootNavController,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }

            composable(BottomNavItem.Colleges.route) {
                PlaceholderScreen("Colleges & Search")
            }

            composable(BottomNavItem.Exam.route) {
                PlaceholderScreen("Upcoming Exam")
            }

            // 🔥 COURSE TAB WIRED HERE
            composable(BottomNavItem.Course.route) {
                CourseScreen(course = sampleCourse)
            }

            composable(BottomNavItem.Profile.route) {
                PlaceholderScreen("User Profile")
            }
        }
    }
}

/* -------------------- COURSE TABS -------------------- */

sealed class CourseDetailTab(val route: String, val label: String) {
    object Overview : CourseDetailTab("overview", "Overview")
    object Eligibility : CourseDetailTab("eligibility", "Eligibility")
    object Duration : CourseDetailTab("duration", "Duration")
    object Fees : CourseDetailTab("fees", "Fees")
    object FutureScope : CourseDetailTab("future_scope", "Future Scope")
}

/* -------------------- COURSE SCREEN -------------------- */

@Composable
fun CourseScreen(course: Course) {
    val courseNavController = rememberNavController()

    val tabs = listOf(
        CourseDetailTab.Overview,
        CourseDetailTab.Eligibility,
        CourseDetailTab.Duration,
        CourseDetailTab.Fees,
        CourseDetailTab.FutureScope
    )

    val navBackStackEntry by courseNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: CourseDetailTab.Overview.route

    Column {
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.route == currentRoute }
                .coerceAtLeast(0)
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = currentRoute == tab.route,
                    onClick = {
                        courseNavController.navigate(tab.route) {
                            launchSingleTop = true
                        }
                    },
                    text = { Text(tab.label) }
                )
            }
        }

        NavHost(
            navController = courseNavController,
            startDestination = CourseDetailTab.Overview.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(CourseDetailTab.Overview.route) {
                CourseOverviewTab(course)
            }
            composable(CourseDetailTab.Eligibility.route) {
                CourseTextTab("Eligibility", course.eligibility)
            }
            composable(CourseDetailTab.Duration.route) {
                CourseTextTab("Duration", course.duration)
            }
            composable(CourseDetailTab.Fees.route) {
                CourseTextTab("Average Fees", course.averageFees)
            }
            composable(CourseDetailTab.FutureScope.route) {
                CourseTextTab("Future Scope", course.futureScope)
            }
        }
    }
}

/* -------------------- COURSE UI -------------------- */

@Composable
fun CourseOverviewTab(course: Course) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = course.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CourseTextTab(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
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