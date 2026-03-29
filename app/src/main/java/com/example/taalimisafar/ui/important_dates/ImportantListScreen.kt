package com.example.taalimisafar.ui.important_dates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.ImportantProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ImportantDatesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportantListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: ImportantDatesViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val allPrograms by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<String?>(null) } // <-- Filter State Added

    LaunchedEffect(categoryId) {
        viewModel.fetchPrograms(categoryId)
    }

    // --- COMBINED FILTER & SEARCH LOGIC ---
    val filteredPrograms = allPrograms.filter { p ->
        val matchesSearch = searchQuery.isBlank() ||
                p.title.contains(searchQuery, ignoreCase = true) ||
                (p.title_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.title_ur?.contains(searchQuery, ignoreCase = true) == true)

        val matchesFilter = when (selectedFilter) {
            "Featured" -> p.is_featured
            "Free" -> {
                val fee = p.application_fee?.lowercase() ?: ""
                fee == "0" || fee.contains("free") || fee.contains("nil") || fee.contains("none")
            }
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4F46E5))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search exams or dates...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4F46E5),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // --- FILTER CHIPS UI ADDED ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEEF2FF),
                        selectedLabelColor = Color(0xFF4F46E5)
                    )
                )
                FilterChip(
                    selected = selectedFilter == "Featured",
                    onClick = { selectedFilter = if (selectedFilter == "Featured") null else "Featured" },
                    label = { Text("Top Exams") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEEF2FF),
                        selectedLabelColor = Color(0xFF4F46E5)
                    )
                )
                FilterChip(
                    selected = selectedFilter == "Free",
                    onClick = { selectedFilter = if (selectedFilter == "Free") null else "Free" },
                    label = { Text("No Fee") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEEF2FF),
                        selectedLabelColor = Color(0xFF4F46E5)
                    )
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading && allPrograms.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF4F46E5))
                } else if (filteredPrograms.isEmpty() && !isLoading) {
                    Text("No programs found.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredPrograms) { program ->
                            ImportantProgramCard(program, language) { onProgramClick(it.id) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImportantProgramCard(program: ImportantProgram, lang: AppLanguage, onClick: (ImportantProgram) -> Unit) {
    val title = when (lang) {
        AppLanguage.HINDI -> program.title_hi ?: program.title
        AppLanguage.URDU -> program.title_ur ?: program.title
        else -> program.title
    }

    // Grab the first available important date to create urgency!
    val nextDeadline = program.important_dates?.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(program) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Image Box
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!program.image.isNullOrBlank() && program.image != "null") {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(if (program.image.startsWith("http")) program.image else "http://10.0.2.2:8000" + program.image)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Default Icon",
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Text Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = program.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), maxLines = 2, overflow = TextOverflow.Ellipsis)

                    val transTitle = when (lang) { AppLanguage.HINDI -> program.title_hi; AppLanguage.URDU -> program.title_ur; else -> null }
                    if (!transTitle.isNullOrBlank() && transTitle != program.title) {
                        Text(text = transTitle, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Chips Row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (program.is_featured) {
                            Surface(color = Color(0xFFFEF3C7), shape = RoundedCornerShape(6.dp)) {
                                Text("FEATURED", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            }
                        }
                        if (!program.application_fee.isNullOrBlank()) {
                            Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(6.dp)) {
                                Text(program.application_fee, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                            }
                        }
                    }
                }
            }

            // --- THE "URGENCY" BADGE ---
            if (nextDeadline != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${nextDeadline.title}: ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFDC2626)
                        )
                        Text(
                            text = nextDeadline.date,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB91C1C)
                        )
                    }
                }
            }
        }
    }
}