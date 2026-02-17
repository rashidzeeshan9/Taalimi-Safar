package com.example.taalimisafar.ui.scholarships

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ScholarshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScholarshipScreen(
    navController: NavController,
    categoryId: Int,
    typeId: Int,
    pageTitle: String,
    viewModel: ScholarshipViewModel = viewModel()
) {
    val scholarships by viewModel.scholarships.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value

    LaunchedEffect(categoryId, typeId) {
        viewModel.fetchScholarships(categoryId = categoryId, typeId = typeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pageTitle, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8F9FA))) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1A237E))
            }
            else if (scholarships.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Info, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))

                    val emptyMsg = when (currentLanguage) {
                        AppLanguage.HINDI -> "No Scholarships Found / कोई छात्रवृत्ति नहीं मिली"
                        AppLanguage.URDU -> "No Scholarships Found / کوئی اسکالرشپ نہیں ملی"
                        else -> "No scholarships available."
                    }
                    Text(text = emptyMsg, textAlign = TextAlign.Center, color = Color.Gray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { viewModel.fetchScholarships(categoryId, typeId) }) {
                        Icon(Icons.Default.Refresh, null)
                        Text(" Try Again")
                    }
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(scholarships) { scholarship ->

                        val displayTitle = when (currentLanguage) {
                            AppLanguage.HINDI -> if (!scholarship.titleHi.isNullOrBlank()) "${scholarship.title} / ${scholarship.titleHi}" else scholarship.title
                            AppLanguage.URDU -> if (!scholarship.titleUr.isNullOrBlank()) "${scholarship.title} / ${scholarship.titleUr}" else scholarship.title
                            else -> scholarship.title
                        }

                        val displayAmount = when (currentLanguage) {
                            AppLanguage.HINDI -> if (!scholarship.amountHi.isNullOrBlank()) "${scholarship.amount} / ${scholarship.amountHi}" else scholarship.amount
                            AppLanguage.URDU -> if (!scholarship.amountUr.isNullOrBlank()) "${scholarship.amount} / ${scholarship.amountUr}" else scholarship.amount
                            else -> scholarship.amount
                        }
                        val isActive = scholarship.status.equals("Active", ignoreCase = true)
                        val statusBgColor = if (isActive) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                        val statusTextColor = if (isActive) Color(0xFF2E7D32) else Color.DarkGray

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSelectedScholarship(scholarship)
                                    navController.navigate("scholarship_detail")
                                },
                            shape = RoundedCornerShape(16.dp), // Rounder edges
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8EAF6)), // Light blue background
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "Scholarship Icon",
                                        tint = Color(0xFF1A237E)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = displayTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Amount: $displayAmount",
                                        color = Color.DarkGray,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(statusBgColor)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = scholarship.status,
                                            color = statusTextColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "View Details",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}