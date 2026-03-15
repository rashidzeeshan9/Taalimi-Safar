package com.example.taalimisafar.ui.CareerAndIndustry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.IndustryProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.IndustryViewModel
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule

private val IndigoPrimary = Color(0xFF4F46E5)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: IndustryViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val programs by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Filter State
    var selectedDemand by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryId, selectedDemand) {
        viewModel.fetchPrograms(categoryId = if (categoryId == 0) null else categoryId, demandLevel = selectedDemand)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(padding)) {

            // Demand Level Filters
            LazyRow(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { DemandChip("All", selectedDemand == null) { selectedDemand = null } }
                item { DemandChip("High Demand", selectedDemand == "High") { selectedDemand = "High" } }
                item { DemandChip("Medium Demand", selectedDemand == "Medium") { selectedDemand = "Medium" } }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = IndigoPrimary)
                }
            } else if (programs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No careers found for this category.", color = TextMuted)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(programs) { program ->
                        IndustryCard(program, currentLanguage) { onProgramClick(program.id) }
                    }
                }
            }
        }
    }
}

@Composable
fun DemandChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) IndigoPrimary else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// --- UPGRADED DUAL-LANGUAGE INDUSTRY CARD ---
@Composable
fun IndustryCard(program: IndustryProgram, language: AppLanguage, onClick: () -> Unit) {
    // Get translations
    val titleTrans = when (language) {
        AppLanguage.HINDI -> program.title_hi
        AppLanguage.URDU -> program.title_ur
        else -> null
    }

    val overviewTrans = when (language) {
        AppLanguage.HINDI -> program.overview_hi
        AppLanguage.URDU -> program.overview_ur
        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- TOP SECTION: Image & Titles ---
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = BackgroundSlate,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    val imgUrl = program.image?.let { if (it.startsWith("http")) it else "http://10.0.2.2:8000$it" }
                    if (!imgUrl.isNullOrBlank()) {
                        AsyncImage(model = imgUrl, contentDescription = null, contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Work, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.padding(14.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 1. English Title (Always on top)
                    Text(
                        text = program.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // 2. Translated Title (Stacked underneath)
                    if (!titleTrans.isNullOrBlank() && titleTrans.trim() != program.title.trim()) {
                        Text(
                            text = titleTrans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = IndigoPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // --- MIDDLE SECTION: Dual-Language Overview ---
            Column(modifier = Modifier.fillMaxWidth()) {
                // 1. English Overview
                if (!program.overview.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = program.overview,
                        fontSize = 13.sp,
                        color = TextMuted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }

                // 2. Translated Overview (Stacked directly underneath)
                if (!overviewTrans.isNullOrBlank() && overviewTrans.trim() != program.overview?.trim()) {
                    Spacer(modifier = Modifier.height(if (program.overview.isNullOrBlank()) 12.dp else 4.dp))
                    Text(
                        text = overviewTrans,
                        fontSize = 13.sp,
                        color = IndigoPrimary, // Matches the blue translation style of the title!
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            // --- BOTTOM SECTION: Stats Row (Demand, Salary, Duration) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Demand Badge
                if (!program.demand_level.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = "Demand", tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(program.demand_level, fontSize = 12.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                }

                // Salary Badge
                if (!program.average_salary.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payments, contentDescription = "Salary", tint = TextMuted, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(program.average_salary, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium)
                    }
                }

                // Duration Badge
                if (!program.duration.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = "Duration", tint = TextMuted, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(program.duration, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}