package com.example.taalimisafar.ui.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.SkillProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: SkillViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val allPrograms by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value

    var searchQuery by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryId) {
        viewModel.fetchPrograms(categoryId)
    }

    val filteredPrograms = allPrograms.filter { p ->
        val matchesSearch = searchQuery.isBlank() ||
                p.name.contains(searchQuery, ignoreCase = true) ||
                (p.name_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.name_ur?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.provider_name?.contains(searchQuery, ignoreCase = true) == true)

        val matchesMode = selectedMode == null || p.mode.equals(selectedMode, ignoreCase = true)
        val matchesLevel = selectedLevel == null || p.level.equals(selectedLevel, ignoreCase = true)

        matchesSearch && matchesMode && matchesLevel
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryName, color = Color.White) },
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
            // SEARCH BAR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search programs...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4F46E5),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // FILTER CHIPS
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                val modes = listOf("online", "offline", "hybrid")
                val levels = listOf("beginner", "intermediate", "advanced")

                items(modes) { mode ->
                    FilterChip(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = if (selectedMode == mode) null else mode },
                        label = { Text(mode.replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFEEF2FF))
                    )
                }
                items(levels) { level ->
                    FilterChip(
                        selected = selectedLevel == level,
                        onClick = { selectedLevel = if (selectedLevel == level) null else level },
                        label = { Text(level.replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFEEF2FF))
                    )
                }
            }

            //LIST CONTENT
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading && allPrograms.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF4F46E5))
                } else if (filteredPrograms.isEmpty() && !isLoading) {
                    Text("No programs found.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredPrograms) { program ->
                            SkillProgramCard(program, currentLanguage) { onProgramClick(it.id) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkillProgramCard(program: SkillProgram, lang: AppLanguage, onClick: (SkillProgram) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(program) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LOGO / DEFAULT ICON
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                if (!program.image.isNullOrEmpty() && program.image != "null") {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (program.image.startsWith("http")) program.image else "http://10.0.2.2:8000" + program.image)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(30.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // TEXT CONTENT
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = program.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                val transTitle = when (lang) {
                    AppLanguage.HINDI -> program.name_hi
                    AppLanguage.URDU -> program.name_ur
                    else -> null
                }
                if (!transTitle.isNullOrBlank() && transTitle != program.name) {
                    Text(
                        text = transTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4F46E5)
                    )
                }

                if (!program.provider_name.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "By: ${program.provider_name}", color = Color(0xFF64748B), fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = program.mode.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5)
                        )
                    }
                    Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = program.level.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5)
                        )
                    }
                }
            }
        }
    }
}