package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.ui.courses.CourseItem
import com.example.taalimisafar.viewmodel.CourseViewModel

// --- DASHBOARD DATA MODEL ---
data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    courseViewModel: CourseViewModel = viewModel()
) {
    val allCourses by courseViewModel.courses.collectAsState()
    val errorMessage by courseViewModel.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val dashboardItems = listOf(
        DashboardItem("Courses", Icons.Default.MenuBook, Color(0xFF1E88E5), "course_tab"),
        DashboardItem("Colleges", Icons.Default.School, Color(0xFF43A047), "colleges_tab"),
        DashboardItem("Exams", Icons.Default.Edit, Color(0xFFE53935), "exam_tab"),
        DashboardItem("Scholarships", Icons.Default.EmojiEvents, Color(0xFF8E24AA), "scholarship_tab"),
        DashboardItem("Counseling", Icons.Default.SupportAgent, Color(0xFF00ACC1), "counseling_tab"),
        DashboardItem("Settings", Icons.Default.Settings, Color(0xFF546E7A), "settings_tab")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // --- HEADER SECTION (Fixed Height) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A237E), Color(0xFF283593))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(28.dp))
                    IconButton(onClick = onThemeToggle) {
                        Icon(if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = "Theme Toggle", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (searchQuery.isEmpty()) "Welcome, Student!" else "Searching...",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (searchQuery.isEmpty()) {
                    Text("Find your path to success", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        // --- SEARCH BAR (Fixed Height) ---
        Box(
            modifier = Modifier
                .offset(y = (-28).dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(25.dp))
                .background(Color.White, RoundedCornerShape(25.dp))
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search 'MCA', 'BTech'...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray) } },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        // --- SCROLLABLE CONTENT AREA ---
        // 🔥 FIX: Added 'weight(1f)' so this Box takes ALL remaining space.
        // This ensures the LazyColumn inside it can scroll properly.
        Box(
            modifier = Modifier
                .weight(1f) // <--- THIS LINE IS CRITICAL
                .fillMaxWidth()
        ) {

            // 1. Error Card
            if (errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                        Text("Connection Failed: $errorMessage", color = Color.Red, fontSize = 12.sp)
                        Button(
                            onClick = { courseViewModel.fetchCourses() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Retry Connection")
                        }
                    }
                }
            }
            // 2. Main Content (Grid or List)
            else {
                if (searchQuery.isBlank()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize() // Ensure Grid fills the Box
                    ) {
                        items(dashboardItems) { item ->
                            DashboardCard(item, navController)
                        }
                    }
                } else {
                    val filteredCourses = allCourses.filter { course ->
                        course.courseName.contains(searchQuery, ignoreCase = true) ||
                                course.description.contains(searchQuery, ignoreCase = true) ||
                                (course.courseName_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                                (course.courseName_ur?.contains(searchQuery, ignoreCase = true) == true)
                    }

                    if (filteredCourses.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No results found for '$searchQuery'", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 16.dp),
                            modifier = Modifier.fillMaxSize() // Ensure List fills the Box
                        ) {
                            items(filteredCourses) { course ->
                                CourseItem(course = course)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper Card
@Composable
fun DashboardCard(item: DashboardItem, navController: NavController) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { navController.navigate(item.route) { launchSingleTop = true } }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                Icon(item.icon, contentDescription = item.title, tint = item.color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}