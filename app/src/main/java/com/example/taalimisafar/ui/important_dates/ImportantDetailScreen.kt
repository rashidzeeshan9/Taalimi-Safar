package com.example.taalimisafar.ui.important_dates

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MenuBook
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
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ImportantDatesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportantDetailScreen(
    programId: Int,
    viewModel: ImportantDatesViewModel,
    onBackClick: () -> Unit
) {
    val program by viewModel.selectedProgram.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value
    val context = LocalContext.current

    LaunchedEffect(programId) {
        viewModel.fetchProgramDetail(programId)
    }

    if (isLoading || program == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF4F46E5))
        }
        return
    }

    val p = program!!

    fun formatDualTitle(en: String, hi: String, ur: String): String {
        val trans = when (language) { AppLanguage.HINDI -> hi; AppLanguage.URDU -> ur; else -> null }
        return if (!trans.isNullOrBlank() && trans != en) "$en / $trans" else en
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(formatDualTitle("Exam Details", "परीक्षा विवरण", "امتحان کی تفصیلات"), fontSize = 18.sp, color = Color.White) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4F46E5))
            )
        },
        bottomBar = {
            if (!p.official_notification.isNullOrBlank()) {
                Surface(color = Color.White, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(p.official_notification))) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp)
                    ) {
                        Text(formatDualTitle("Official Notification", "आधिकारिक अधिसूचना", "سرکاری نوٹیفکیشن"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            if (!p.image.isNullOrBlank() && p.image != "null") {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (p.image.startsWith("http")) p.image else "http://10.0.2.2:8000" + p.image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Default Banner",
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Core Details
            Text(p.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            val titleTrans = when(language) { AppLanguage.HINDI -> p.title_hi; AppLanguage.URDU -> p.title_ur; else -> null }
            if (!titleTrans.isNullOrBlank() && titleTrans != p.title) {
                Text(titleTrans, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!p.age_limit.isNullOrBlank()) InfoChip("Age Limit", p.age_limit)
                if (!p.application_fee.isNullOrBlank()) InfoChip("Fees", p.application_fee)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Sections
            ImportantSection(formatDualTitle("Overview", "अवलोकन", "جائزہ"), p.overview, when(language) { AppLanguage.HINDI -> p.overview_hi; AppLanguage.URDU -> p.overview_ur; else -> null })
            ImportantSection(formatDualTitle("Eligibility", "पात्रता", "اہلیت"), p.eligibility, when(language) { AppLanguage.HINDI -> p.eligibility_hi; AppLanguage.URDU -> p.eligibility_ur; else -> null })
            ImportantSection(formatDualTitle("Selection Process", "चयन प्रक्रिया", "انتخاب کا عمل"), p.selection_process, when(language) { AppLanguage.HINDI -> p.selection_process_hi; AppLanguage.URDU -> p.selection_process_ur; else -> null })

            // Important Dates List
            if (!p.important_dates.isNullOrEmpty()) {
                Text(formatDualTitle("Important Dates", "महत्वपूर्ण तिथियां", "اہم تاریخیں"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.important_dates.forEach { date ->
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF4F46E5))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(date.date, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text(date.title, fontSize = 14.sp, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Syllabus List
            if (!p.program_syllabus.isNullOrEmpty()) {
                Text(formatDualTitle("Syllabus", "पाठ्यक्रम", "نصاب"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.program_syllabus.forEach { s ->
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF1976D2))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                val subjectTrans = when(language) { AppLanguage.HINDI -> s.subject_hi; AppLanguage.URDU -> s.subject_ur; else -> null }
                                Text(if (!subjectTrans.isNullOrBlank()) "${s.subject} / $subjectTrans" else s.subject, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))

                                val topicsTrans = when(language) { AppLanguage.HINDI -> s.topics_hi; AppLanguage.URDU -> s.topics_ur; else -> null }
                                Text(topicsTrans ?: (s.topics ?: ""), fontSize = 14.sp, color = Color(0xFF334155), modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// Reusable Components
@Composable
private fun InfoChip(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }
    }
}

@Composable
private fun ImportantSection(title: String, enContent: String?, transContent: String?) {
    if (enContent.isNullOrBlank()) return
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    Spacer(modifier = Modifier.height(8.dp))
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            val enLines = enContent.split(Regex("[\n.]")).map { it.trim() }.filter { it.isNotEmpty() }
            val transLines = transContent?.split(Regex("[\n।.]"))?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            val maxLines = maxOf(enLines.size, transLines.size)

            for (i in 0 until maxLines) {
                val enLine = enLines.getOrNull(i)
                val transLine = transLines.getOrNull(i)
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    if (enLine != null) Text("• $enLine", fontSize = 14.sp, color = Color(0xFF334155))
                    if (transLine != null && transLine != enLine) Text("  $transLine", fontSize = 14.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}