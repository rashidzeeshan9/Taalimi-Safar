package com.example.taalimisafar.ui.women

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VolunteerActivism
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
import com.example.taalimisafar.data.model.WomenProgram
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.WomenViewModel

// Theme Colors
private val PinkPrimary = Color(0xFFE11D48)
private val PinkLight = Color(0xFFFFE4E6)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WomenListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: WomenViewModel,
    onBackClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val programs by viewModel.programs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Filter State
    var selectedMode by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryId) {
        viewModel.fetchPrograms(if (categoryId == 0) null else categoryId)
    }

    // Filter Logic
    val filteredPrograms = remember(programs, selectedMode) {
        programs.filter { program ->
            selectedMode == null || program.mode.equals(selectedMode, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkPrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(paddingValues)
        ) {
            // FILTER ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedMode == null,
                    onClick = { selectedMode = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PinkLight, selectedLabelColor = PinkPrimary)
                )
                FilterChip(
                    selected = selectedMode == "Online",
                    onClick = { selectedMode = if (selectedMode == "Online") null else "Online" },
                    label = { Text("Online") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PinkLight, selectedLabelColor = PinkPrimary)
                )
                FilterChip(
                    selected = selectedMode == "Offline",
                    onClick = { selectedMode = if (selectedMode == "Offline") null else "Offline" },
                    label = { Text("Offline") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PinkLight, selectedLabelColor = PinkPrimary)
                )
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PinkPrimary)
                }
            } else if (filteredPrograms.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No programs found.", color = TextMuted)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPrograms) { program ->
                        WomenCard(program, currentLanguage) { onProgramClick(program.id) }
                    }
                }
            }
        }
    }
}

@Composable
fun WomenCard(program: WomenProgram, currentLanguage: AppLanguage, onClick: () -> Unit) {
    fun getTrans(hi: String?, ur: String?): String? = when (currentLanguage) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp), color = PinkLight) {
                    val imageUrl = program.image?.let { img -> if (img.startsWith("http")) img else "http://10.0.2.2:8000$img" }
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(),
                            contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.VolunteerActivism, null, tint = PinkPrimary, modifier = Modifier.padding(12.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = program.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 2, overflow = TextOverflow.Ellipsis)

                    val transTitle = getTrans(program.title_hi, program.title_ur)
                    if (!transTitle.isNullOrBlank()) {
                        Text(text = transTitle, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PinkPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            // Bottom Tags (Mode & Duration)
            if (!program.mode.isNullOrBlank() || !program.duration.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if (!program.mode.isNullOrBlank()) {
                        Surface(color = PinkLight, shape = RoundedCornerShape(6.dp)) {
                            Text(text = program.mode.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PinkPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    if (!program.duration.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(program.duration, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}