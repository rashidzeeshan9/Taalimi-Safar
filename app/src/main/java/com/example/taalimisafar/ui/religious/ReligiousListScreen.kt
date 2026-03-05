package com.example.taalimisafar.ui.religious

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.QuestionAnswer
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.ReligiousProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ReligiousViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReligiousListScreen(
    religionId: Int,
    religionName: String,
    viewModel: ReligiousViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val allPrograms by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value
    var searchQuery by remember { mutableStateOf("") }
    var filterHasVideo by remember { mutableStateOf(false) }
    var filterHasFaq by remember { mutableStateOf(false) }

    LaunchedEffect(religionId) {
        viewModel.fetchPrograms(religionId)
    }
    val filteredPrograms = allPrograms.filter { p ->
        val matchesSearch = searchQuery.isBlank() ||
                p.program_name.contains(searchQuery, ignoreCase = true) ||
                (p.program_name_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.program_name_ur?.contains(searchQuery, ignoreCase = true) == true)

        val matchesVideo = !filterHasVideo || p.videos.isNotEmpty()
        val matchesFaq = !filterHasFaq || p.faqs.isNotEmpty()

        matchesSearch && matchesVideo && matchesFaq
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(religionName, color = Color.White) },
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
            // --- SEARCH BAR ---
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

            // --- SMART FILTER CHIPS ---
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filterHasVideo,
                        onClick = { filterHasVideo = !filterHasVideo },
                        label = { Text("Has Video") },
                        leadingIcon = { Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFEEF2FF))
                    )
                }
                item {
                    FilterChip(
                        selected = filterHasFaq,
                        onClick = { filterHasFaq = !filterHasFaq },
                        label = { Text("Has FAQs") },
                        leadingIcon = { Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFEEF2FF))
                    )
                }
            }

            // --- LIST CONTENT ---
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading && allPrograms.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF4F46E5))
                } else if (filteredPrograms.isEmpty() && !isLoading) {
                    Text("No programs found.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredPrograms) { program ->
                            ReligiousProgramCard(program, language) { onProgramClick(it.id) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReligiousProgramCard(program: ReligiousProgram, lang: AppLanguage, onClick: (ReligiousProgram) -> Unit) {
    val title = when (lang) {
        AppLanguage.HINDI -> program.program_name_hi ?: program.program_name
        AppLanguage.URDU -> program.program_name_ur ?: program.program_name
        else -> program.program_name
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(program) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

            if (!program.image.isNullOrBlank() && program.image != "null") {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (program.image.startsWith("http")) program.image else "http://10.0.2.2:8000" + program.image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = program.program_name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))

                val transTitle = when (lang) { AppLanguage.HINDI -> program.program_name_hi; AppLanguage.URDU -> program.program_name_ur; else -> null }
                if (!transTitle.isNullOrBlank() && transTitle != program.program_name) {
                    Text(text = transTitle, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
                }

                Spacer(modifier = Modifier.height(4.dp))

                val desc = when (lang) { AppLanguage.HINDI -> program.description_hi; AppLanguage.URDU -> program.description_ur; else -> null } ?: program.description
                Text(text = desc, fontSize = 13.sp, color = Color(0xFF64748B), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}