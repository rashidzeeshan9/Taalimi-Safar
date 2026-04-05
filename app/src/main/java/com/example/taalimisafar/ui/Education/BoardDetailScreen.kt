package com.example.taalimisafar.ui.education

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taalimisafar.viewmodel.EducationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(boardId: Int, navController: NavController, viewModel: EducationViewModel) {
    val board by viewModel.selectedBoard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(boardId) {
        viewModel.fetchBoardDetail(boardId)
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = { Text(board?.name ?: "Loading...", color = Color.White, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        }
    ) { padding ->
        if (isLoading || board == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1A237E))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overview Section
                if (!board!!.overview.isNullOrBlank()) {
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Overview", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A237E))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(board!!.overview ?: "", fontSize = 14.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }

                // Programs Section
                if (board!!.programs.isNotEmpty()) {
                    item { Text("Available Programs", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp)) }
                    items(board!!.programs) { program ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Navigate to program detail */ }
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE8DEF8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF6750A4))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(program.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("Class: ${program.class_level}", fontSize = 13.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                // News & Updates Section
                if (board!!.news.isNotEmpty()) {
                    item { Text("Latest Updates", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp)) }
                    items(board!!.news) { news ->
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Article, contentDescription = null, tint = Color(0xFF1A237E))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(news.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(news.date, fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}