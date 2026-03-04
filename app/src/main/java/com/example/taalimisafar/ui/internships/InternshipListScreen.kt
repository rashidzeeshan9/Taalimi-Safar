package com.example.taalimisafar.ui.internships

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.Internship
import com.example.taalimisafar.viewmodel.InternshipViewModel
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.AppStrings

val IndigoPrimary = Color(0xFF4F46E5)
val IndigoLight = Color(0xFFEEF2FF)
val BackgroundSlate = Color(0xFFF8FAFC)
val TextDark = Color(0xFF1E293B)
val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: InternshipViewModel,
    onBackClick: () -> Unit,
    onInternshipClick: (Internship) -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val internships by viewModel.internships.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(categoryId) { viewModel.fetchInternships(categoryId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = IndigoPrimary)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(internships) { internship ->
                        InternshipItemCard(
                            internship = internship,
                            language = currentLanguage,
                            onClick = { onInternshipClick(internship) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InternshipItemCard(internship: Internship, language: AppLanguage, onClick: () -> Unit) {
    fun getTrans(hi: String?, ur: String?): String? = when (language) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)) // Clean modern border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = IndigoLight) {
                    if (!internship.image.isNullOrEmpty() && internship.image != "null") {
                        val imageUrl = if (internship.image.startsWith("http")) internship.image else "http://10.0.2.2:8000" + internship.image
                        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.padding(14.dp), tint = IndigoPrimary)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    DualLineTextList(
                        enText = internship.Intership_title,
                        transText = getTrans(internship.Intership_title_hi, internship.Intership_title_ur),
                        isTitle = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DualLineTextList(
                        enText = internship.Organization_name,
                        transText = getTrans(internship.Organization_name_hi, internship.Organization_name_ur),
                        isSub = true
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF1F5F9))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextMuted)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Location", fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                    }
                    DualLineTextList(enText = internship.Location, transText = getTrans(internship.Location_hi, internship.Location_ur), isInfo = true)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextMuted)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = AppStrings.getLabel("Duration", language), fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                    }
                    DualLineTextList(enText = internship.duration, transText = getTrans(internship.duration_hi, internship.duration_ur), isInfo = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(color = IndigoLight, shape = RoundedCornerShape(8.dp)) {
                    Text(text = internship.mode.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }

                Column(horizontalAlignment = Alignment.End) {
                    val stipendText = if (internship.stipend_amount.isNullOrBlank() || internship.stipend_amount == "0") "Unpaid" else "₹${internship.stipend_amount}"
                    Text(text = stipendText, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981)) // Emerald green for money
                    if (!internship.last_date_to_apply.isNullOrBlank()) {
                        Text(text = "Apply by: ${internship.last_date_to_apply}", fontSize = 11.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium) // Red for urgency
                    }
                }
            }
        }
    }
}

@Composable
fun DualLineTextList(enText: String?, transText: String?, isTitle: Boolean = false, isSub: Boolean = false, isInfo: Boolean = false) {
    Column {
        if (!enText.isNullOrBlank() && enText != "null") {
            Text(
                text = enText,
                fontSize = if (isTitle) 17.sp else if (isSub) 14.sp else 13.sp,
                fontWeight = if (isTitle) FontWeight.Bold else if (isInfo) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSub) TextMuted else TextDark,
                lineHeight = if (isTitle) 22.sp else 18.sp
            )
        }
        if (!transText.isNullOrBlank() && transText != "null") {
            Text(
                text = transText,
                fontSize = if (isTitle) 15.sp else if (isSub) 13.sp else 13.sp,
                color = IndigoPrimary,
                fontWeight = if (isTitle) FontWeight.Bold else FontWeight.Medium,
                lineHeight = if (isTitle) 20.sp else 18.sp
            )
        }
    }
}