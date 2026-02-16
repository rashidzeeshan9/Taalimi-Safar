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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.ui.courses.CourseItem
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.CourseViewModel

data class DashboardItem(
    val titleEn: String,
    val titleHi: String,
    val titleUr: String,
    val icon: ImageVector,
    val color: Color, // Icon Color
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
    // Data & State
    val allCourses by courseViewModel.courses.collectAsState()
    val errorMessage by courseViewModel.errorMessage.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value
    var searchQuery by remember { mutableStateOf("") }
// Dashboard category
    val dashboardItems = remember {
        listOf(
            DashboardItem("Academic", "शैक्षणिक", "تعلیمی", Icons.Default.School, Color(0xFFFF6D00), "academic"),
            DashboardItem("Diploma", "डिप्लोमा", "ڈپلومہ", Icons.Default.MenuBook, Color(0xFF2962FF), "diploma"),
            DashboardItem("Women Empowerment", "महिला सशक्तिकरण", "خواتین کو بااختیار بنانا", Icons.Default.Female, Color(0xFFD500F9), "women"),
            DashboardItem("Scholarships", "छात्रवृत्ति", "وظائف", Icons.Default.EmojiEvents, Color(0xFF7B1FA2), "scholarship_tab"),
            DashboardItem("Internships", "इंटर्नशिप", "انٹرنشپ", Icons.Default.Work, Color(0xFF00C853), "internships"),
            DashboardItem("Skill Development", "कौशल विकास", "مہارتوں کی ترقی", Icons.Default.Build, Color(0xFFFFAB00), "skills"),
            DashboardItem("Important Dates", "महत्वपूर्ण तिथियां", "اہم تاریخیں", Icons.Default.Event, Color(0xFFF44336), "important_dates"),
            DashboardItem("Govt Jobs", "सरकारी नौकरियां", "سرکاری نوکریاں", Icons.Default.AccountBalance, Color(0xFF455A64), "govt_jobs"),
            DashboardItem("Private Jobs", "निजी नौकरियां", "نجی نوکریاں", Icons.Default.BusinessCenter, Color(0xFF827717), "private_jobs"),
            DashboardItem("Govt Schemes", "सरकारी योजनाएं", "سرکاری اسکیمیں", Icons.Default.Assignment, Color(0xFF009688), "govt_schemes"),
            DashboardItem("Sports Career", "खेल में करियर", "کھیلوں میں کیریئر", Icons.Default.SportsSoccer, Color(0xFFFF5722), "sports"),
            DashboardItem("Good Hobbies", "अच्छी आदतें", "اچھے مشاغل", Icons.Default.Star, Color(0xFF6200EA), "hobbies")
        )
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A237E), Color(0xFF303F9F))
                        )
                    )
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Top Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Taalimi Safar",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            val subtitle = when(currentLanguage) {
                                AppLanguage.HINDI -> "सफलता की ओर आपका रास्ता"
                                AppLanguage.URDU -> "کامیابی کا آپ کا راستہ"
                                else -> "Your path to success"
                            }
                            Text(
                                text = subtitle,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Theme Toggle",
                                tint = Color.White
                            )
                        }
                    }

                    // --- SEARCH BAR ---
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        // ✅ UPDATED PLACEHOLDER
                        placeholder = {
                            Text("Search 'Scholarships', 'MCA'...", color = Color.Gray)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF1A237E))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color(0xFF1A237E)
                        ),
                        singleLine = true
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. ERROR STATE
            if (errorMessage != null && searchQuery.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Connection failed. Check internet.", color = Color.Red)
                }
            }
            // 2. MAIN CONTENT
            else {
                if (searchQuery.isBlank()) {
                    // --- SHOW GRID (Includes Scholarships) ---
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(dashboardItems) { item ->
                            DashboardCard(item, navController, currentLanguage)
                        }
                    }
                } else {
                    // --- GLOBAL SEARCH (Currently searches Courses) ---
                    val filteredCourses = allCourses.filter { course ->
                        course.courseName.contains(searchQuery, ignoreCase = true) ||
                                course.description.contains(searchQuery, ignoreCase = true) ||
                                (course.courseName_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                                (course.courseName_ur?.contains(searchQuery, ignoreCase = true) == true)
                    }

                    if (filteredCourses.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(50.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No results found", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
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

// --- DASHBOARD CARD ---
@Composable
fun DashboardCard(
    item: DashboardItem,
    navController: NavController,
    currentLanguage: AppLanguage
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable {
                try { navController.navigate(item.route) } catch (e: Exception) { e.printStackTrace() }
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
                text = item.titleEn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            if (currentLanguage != AppLanguage.NONE) {
                val translated = if (currentLanguage == AppLanguage.HINDI) item.titleHi else item.titleUr

                Text(
                    text = translated,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (currentLanguage == AppLanguage.URDU) Color(0xFF00695C) else Color(0xFF1565C0),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}