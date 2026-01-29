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
import com.example.taalimisafar.viewmodel.CourseViewModel

@Composable
fun CourseListScreen(
    viewModel: CourseViewModel = viewModel()
) {
    val courses by viewModel.courses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // 1. MAIN CONTAINER (Do NOT add .verticalScroll here!)
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
                modifier = Modifier.align(Alignment.Center).padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                Text("Connection Failed", color = Color.Red, fontWeight = FontWeight.Bold)
                Button(onClick = { viewModel.fetchCourses() }) { Text("Retry") }
            }
        } else {
            // 2. THE SCROLLABLE LIST
            LazyColumn(
                modifier = Modifier.fillMaxSize(), // Takes up full screen
                contentPadding = PaddingValues(bottom = 140.dp) // Space for Bottom Nav
            ) {

                // 3. HEADER IS HERE (Inside the list!)
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "All Courses",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Find the best course for your future",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // 4. THE COURSE ITEMS
                items(courses) { course ->
                    CourseItem(course = course)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}