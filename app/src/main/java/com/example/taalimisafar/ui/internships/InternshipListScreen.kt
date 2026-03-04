package com.example.taalimisafar.ui.internships

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
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
import com.example.taalimisafar.data.model.Internship
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.InternshipViewModel

// Colors
private val IndigoPrimary = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFFE0E7FF)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: InternshipViewModel,
    onBackClick: () -> Unit,
    onInternshipClick: (Int) -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val internships by viewModel.internships.collectAsState()

    // Local state for Internshala-style filters
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    // Filter the list locally based on selected chips
    val filteredInternships = internships.filter { internship ->
        val matchesMode = selectedMode == null || internship.mode.equals(selectedMode, ignoreCase = true)
        val matchesType = selectedType == null || internship.internship_type.equals(selectedType, ignoreCase = true)
        matchesMode && matchesType
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
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
                    selected = selectedMode == null && selectedType == null,
                    onClick = { selectedMode = null; selectedType = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
                )
                FilterChip(
                    selected = selectedMode == "online",
                    onClick = { selectedMode = if (selectedMode == "online") null else "online" },
                    label = { Text("Remote / WFH") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
                )
                FilterChip(
                    selected = selectedType == "Paid",
                    onClick = { selectedType = if (selectedType == "Paid") null else "Paid" },
                    label = { Text("Paid Only") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
                )
                FilterChip(
                    selected = selectedMode == "offline",
                    onClick = { selectedMode = if (selectedMode == "offline") null else "offline" },
                    label = { Text("In-Office") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
                )
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            // INTERNSHIP LIST
            if (filteredInternships.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No internships found for this filter.", color = TextMuted)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredInternships) { internship ->
                        InternshipCard(
                            internship = internship,
                            currentLanguage = currentLanguage,
                            onClick = { onInternshipClick(internship.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InternshipCard(
    internship: Internship,
    currentLanguage: AppLanguage,
    onClick: () -> Unit
) {
    fun getTrans(hi: String?, ur: String?): String? = when (currentLanguage) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Image + Titles
            Row(verticalAlignment = Alignment.Top) {
                // Image or Fallback Logo
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = BackgroundSlate
                ) {
                    val imageUrl = internship.image?.let { img ->
                        if (img.startsWith("http")) img else "http://10.0.2.2:8000$img"
                    }
                    if (!imageUrl.isNullOrBlank() && imageUrl != "null") {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Business, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.padding(12.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Title and Company
                Column(modifier = Modifier.weight(1f)) {
                    // English Title
                    Text(
                        text = internship.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Translated Title
                    val transTitle = getTrans(internship.title_hi, internship.title_ur)
                    if (!transTitle.isNullOrBlank() && transTitle != "null") {
                        Text(
                            text = transTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = IndigoPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = internship.organization_name,
                        fontSize = 14.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tags Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = IndigoLight, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = internship.mode.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(color = if (internship.internship_type == "Paid") Color(0xFFD1FAE5) else BackgroundSlate, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = internship.internship_type.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (internship.internship_type == "Paid") Color(0xFF059669) else TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            // Footer Info
            val stipend = if (internship.stipend_amount.isNullOrBlank() || internship.stipend_amount == "0" || internship.stipend_amount == "0.00") "Unpaid" else "₹${internship.stipend_amount}"
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(internship.location, fontSize = 13.sp, color = TextMuted)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stipend, fontSize = 13.sp, color = TextDark, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}