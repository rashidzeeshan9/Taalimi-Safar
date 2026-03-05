package com.example.taalimisafar.ui.diplomas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.DiplomaProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.DiplomaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiplomaListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: DiplomaViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val allPrograms by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categoryId) {
        viewModel.fetchPrograms(categoryId)
    }

    val filteredPrograms = allPrograms.filter { p ->
        searchQuery.isBlank() ||
                p.name.contains(searchQuery, ignoreCase = true) ||
                (p.name_hi?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.name_ur?.contains(searchQuery, ignoreCase = true) == true) ||
                (p.institute_name?.contains(searchQuery, ignoreCase = true) == true)
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
                placeholder = { Text("Search diplomas or institutes...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4F46E5),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

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
                            DiplomaProgramCard(program, language) { onProgramClick(it.id) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiplomaProgramCard(program: DiplomaProgram, lang: AppLanguage, onClick: (DiplomaProgram) -> Unit) {
    val title = when (lang) {
        AppLanguage.HINDI -> program.name_hi ?: program.name
        AppLanguage.URDU -> program.name_ur ?: program.name
        else -> program.name
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(program) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                if (!program.thumbnail.isNullOrBlank() && program.thumbnail != "null") {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (program.thumbnail.startsWith("http")) program.thumbnail else "http://10.0.2.2:8000" + program.thumbnail)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Default Diploma Icon",
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = program.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))

                val transTitle = when (lang) { AppLanguage.HINDI -> program.name_hi; AppLanguage.URDU -> program.name_ur; else -> null }
                if (!transTitle.isNullOrBlank() && transTitle != program.name) {
                    Text(text = transTitle, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
                }

                if (!program.institute_name.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = program.institute_name, fontSize = 13.sp, color = Color(0xFF64748B), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!program.duration.isNullOrBlank()) {
                        Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                            Text(program.duration, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                        }
                    }
                }
            }
        }
    }
}