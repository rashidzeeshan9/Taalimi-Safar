package com.example.taalimisafar.ui.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.CourseViewModel

@Composable
fun CourseListScreen(
    viewModel: CourseViewModel = viewModel()
) {
    val courses by viewModel.courses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Observe Language Selection
    val selectedLanguage = LanguageManager.currentLanguage.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF1A237E)
            )
        } else if (errorMessage != null) {
            // Error View
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Connection Failed", color = Color.Red, fontWeight = FontWeight.Bold)
                Button(
                    onClick = { viewModel.fetchCourses() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                    modifier = Modifier.padding(top = 8.dp)
                ) { Text("Retry") }
            }
        } else {
            // THE SCROLLABLE LIST
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {

                // HEADER ITEM (Title + Language Buttons)
                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)) {
                        Text(
                            text = "All Courses",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Select your preferred language:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- LANGUAGE BUTTONS ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LangFilterChip(
                                text = "English",
                                isSelected = selectedLanguage == AppLanguage.NONE,
                                onClick = { LanguageManager.currentLanguage.value = AppLanguage.NONE }
                            )
                            LangFilterChip(
                                text = "हिंदी (Hindi)",
                                isSelected = selectedLanguage == AppLanguage.HINDI,
                                onClick = { LanguageManager.currentLanguage.value = AppLanguage.HINDI }
                            )
                            LangFilterChip(
                                text = "اردو (Urdu)",
                                isSelected = selectedLanguage == AppLanguage.URDU,
                                onClick = { LanguageManager.currentLanguage.value = AppLanguage.URDU }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // THE COURSE ITEMS
                items(courses) { course ->
                    // This calls the SEPARATE file
                    CourseItem(course = course)
                }
            }
        }
    }
}

// Helper Component for Buttons
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LangFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFE8EAF6),
            selectedLabelColor = Color(0xFF1A237E)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) Color(0xFF1A237E) else Color.Gray
        )
    )
}