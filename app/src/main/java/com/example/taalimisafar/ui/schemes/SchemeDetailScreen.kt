package com.example.taalimisafar.ui.schemes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.Schemes
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import kotlin.math.max

// Colors
private val IndigoPrimary = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFFE0E7FF)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeDetailScreen(
    scheme: Schemes?,
    onBackClick: () -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scheme Details", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { paddingValues ->
        if (scheme == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IndigoPrimary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section (White Card WITH IMAGE)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Scheme Logo
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = BackgroundSlate
                    ) {
                        val imageUrl = scheme.image?.let { img ->
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
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.padding(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Title Column
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = scheme.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            lineHeight = 24.sp
                        )

                        val transTitle = getTrans(scheme.title_hi, scheme.title_ur, currentLanguage)
                        if (!transTitle.isNullOrBlank() && transTitle != "null") {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = transTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = IndigoPrimary,
                                lineHeight = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Introduced by: ${scheme.introduced_by}", color = TextMuted, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Content (Line-by-Line Translation)
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                TranslatedSection(
                    icon = Icons.Default.Info,
                    title = "About Scheme",
                    englishBody = scheme.description,
                    translatedBody = getTrans(scheme.description_hi, scheme.description_ur, currentLanguage)
                )

                TranslatedSection(
                    icon = Icons.Default.Star,
                    title = "Benefits",
                    englishBody = scheme.benefits,
                    translatedBody = getTrans(scheme.benefits_hi, scheme.benefits_ur, currentLanguage)
                )

                TranslatedSection(
                    icon = Icons.Default.Rule,
                    title = "Eligibility",
                    englishBody = scheme.eligibility,
                    translatedBody = getTrans(scheme.eligibility_hi, scheme.eligibility_ur, currentLanguage)
                )

                TranslatedSection(
                    icon = Icons.Default.Description,
                    title = "Application Process",
                    englishBody = scheme.application_process,
                    translatedBody = getTrans(scheme.application_process_hi, scheme.application_process_ur, currentLanguage)
                )

                // Extra Contact Info Card
                if (!scheme.helpline_number.isNullOrBlank() || scheme.official_link.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = IndigoLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (!scheme.helpline_number.isNullOrBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Helpline: ${scheme.helpline_number}", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (scheme.official_link.isNotEmpty()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Language, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Website: ${scheme.official_link}", color = IndigoPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

fun getTrans(hi: String?, ur: String?, currentLanguage: AppLanguage): String? = when (currentLanguage) {
    AppLanguage.HINDI -> hi
    AppLanguage.URDU -> ur
    else -> null
}

@Composable
fun TranslatedSection(
    icon: ImageVector,
    title: String,
    englishBody: String?,
    translatedBody: String?
) {
    if (!englishBody.isNullOrBlank() && englishBody != "null") {
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Section Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- TRUE LINE-BY-LINE TRANSLATION LOGIC ---
                // Split the text into individual paragraphs based on new lines
                val englishLines = englishBody.split("\n").filter { it.isNotBlank() }
                val translatedLines = translatedBody?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()

                // Figure out which list is longer so we don't miss any lines
                val totalLines = max(englishLines.size, translatedLines.size)

                for (i in 0 until totalLines) {
                    val engLine = englishLines.getOrNull(i)
                    val transLine = translatedLines.getOrNull(i)

                    Column(modifier = Modifier.padding(bottom = 12.dp)) {
                        // Print the English line
                        if (engLine != null) {
                            Text(text = engLine.trim(), fontSize = 14.sp, color = TextDark, lineHeight = 20.sp)
                        }

                        // Print the ONE matching translated line right underneath it
                        if (transLine != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = transLine.trim(),
                                fontSize = 14.sp,
                                color = IndigoPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}