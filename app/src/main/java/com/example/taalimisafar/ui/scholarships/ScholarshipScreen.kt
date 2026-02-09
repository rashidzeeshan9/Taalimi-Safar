package com.example.taalimisafar.ui.scholarships

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ScholarshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScholarshipScreen(
    navController: NavController,
    viewModel: ScholarshipViewModel = viewModel()
) {
    // 1. Collect Data from ViewModel
    val scholarships by viewModel.scholarships.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value

    Scaffold(
        // --- TOP BAR (Professional Blue) ---
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Scholarships", fontWeight = FontWeight.Bold)
                        Text("Find financial aid / छात्रवृत्ति", fontSize = 12.sp, fontWeight = FontWeight.Normal)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E), // Matches your App Theme
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F7FA) // Light Grey Background
    ) { padding ->

        // --- CONTENT ---
        if (scholarships.isEmpty()) {
            // Empty State (Loading or No Data)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1A237E))
            }
        } else {
            // List of Scholarship Cards
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(scholarships) { item ->
                    // ✅ CALLING YOUR ITEM HERE
                    ScholarshipItem(item = item, currentLanguage = currentLanguage)
                }
            }
        }
    }
}