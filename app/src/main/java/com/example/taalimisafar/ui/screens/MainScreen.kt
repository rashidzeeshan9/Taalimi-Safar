package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taalimisafar.ui.courses.CourseListScreen

/* -------------------- BOTTOM NAV -------------------- */

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_tab", "Home", Icons.Default.Home)
    object Colleges : BottomNavItem("colleges_tab", "Education/School", Icons.Default.School)
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
                EducationScreen(navController = rootNavController)
            }

            composable(BottomNavItem.Exam.route) {
                PlaceholderScreen("Upcoming Exam")
            }

            composable(BottomNavItem.Course.route) {
                CourseListScreen()
            }

            composable(BottomNavItem.Profile.route) {
                PlaceholderScreen("User Profile")
            }
        }
    }
}

/* -------------------- EDUCATION SCREEN -------------------- */

data class EducationItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(navController: NavController) {

    val educationBoards = listOf(
        EducationItem("CBSE", "All India", Icons.Default.School, Color(0xFF1E88E5), "cbse"),
        EducationItem("NIOS", "All India", Icons.Default.MenuBook, Color(0xFF43A047), "nios"),
        EducationItem("PSEB", "Punjab", Icons.Default.AccountBalance, Color(0xFFF4511E), "pseb"),
        EducationItem("UPMSP", "Uttar Pradesh", Icons.Default.LocationOn, Color(0xFF6D4C41), "upmsp"),
        EducationItem("MSBSHSE", "Maharashtra", Icons.Default.Groups, Color(0xFF8E24AA), "msbshse"),
        EducationItem("BSEH", "Haryana", Icons.Default.Assignment, Color(0xFF00897B), "bseh"),
        EducationItem("Jamia", "New Delhi", Icons.Default.AccountBalance, Color(0xFFD81B60), "jamia"),
        EducationItem("BSEB", "Bihar", Icons.Default.Description, Color(0xFF3949AB), "bseb")
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Education / School",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(educationBoards) { item ->
                EducationCard(item = item, navController = navController)
            }
        }
    }
}

/* -------------------- EDUCATION CARD -------------------- */

@Composable
fun EducationCard(
    item: EducationItem,
    navController: NavController
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable {
                try {
                    navController.navigate(item.route)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center
            )

            Text(
                text = item.subtitle,
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
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