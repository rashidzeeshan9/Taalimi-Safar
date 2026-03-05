package com.example.taalimisafar.ui.diplomas

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.School
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
import com.example.taalimisafar.viewmodel.DiplomaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiplomaDetailScreen(
    programId: Int,
    viewModel: DiplomaViewModel,
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
                title = { Text(formatDualTitle("Diploma Details", "डिप्लोमा विवरण", "ڈپلومہ کی تفصیلات"), fontSize = 18.sp, color = Color.White) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4F46E5))
            )
        },
        bottomBar = {
            if (!p.apply_link.isNullOrBlank()) {
                Surface(color = Color.White, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(p.apply_link))) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp)
                    ) {
                        Text(formatDualTitle("Apply Now", "अभी आवेदन करें", "ابھی اپلائی کریں"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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

            if (!p.thumbnail.isNullOrBlank() && p.thumbnail != "null") {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (p.thumbnail.startsWith("http")) p.thumbnail else "http://10.0.2.2:8000" + p.thumbnail)
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
                        imageVector = Icons.Default.School,
                        contentDescription = "Default Diploma Banner",
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Core Details
            Text(p.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            val titleTrans = when(language) { AppLanguage.HINDI -> p.name_hi; AppLanguage.URDU -> p.name_ur; else -> null }
            if (!titleTrans.isNullOrBlank() && titleTrans != p.name) {
                Text(titleTrans, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoChip("Duration", p.duration ?: "N/A")
                InfoChip("Fees", p.fees ?: "N/A")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Sections
            DiplomaSection(formatDualTitle("Description", "विवरण", "تفصیل"), p.description, when(language) { AppLanguage.HINDI -> p.description_hi; AppLanguage.URDU -> p.description_ur; else -> null })
            DiplomaSection(formatDualTitle("Eligibility", "पात्रता", "اہلیت"), p.eligibility, when(language) { AppLanguage.HINDI -> p.eligibility_hi; AppLanguage.URDU -> p.eligibility_ur; else -> null })
            DiplomaSection(formatDualTitle("Admission Process", "प्रवेश प्रक्रिया", "داخلہ کا عمل"), p.admission_process, when(language) { AppLanguage.HINDI -> p.admission_process_hi; AppLanguage.URDU -> p.admission_process_ur; else -> null })
            DiplomaSection(formatDualTitle("Career Opportunities", "कैरियर के अवसर", "کیریئر کے مواقع"), p.career_opportunities, when(language) { AppLanguage.HINDI -> p.career_opportunities_hi; AppLanguage.URDU -> p.career_opportunities_ur; else -> null })

            // Institutes List
            if (p.institutes.isNotEmpty()) {
                Text(formatDualTitle("Institutes", "संस्थान", "ادارے"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.institutes.forEach { inst ->
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF4F46E5))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(inst.institute_name, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text("${inst.institute_city}, ${inst.institute_state}", fontSize = 14.sp, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Resources List
            if (p.resources.isNotEmpty()) {
                Text(formatDualTitle("Study Resources", "अध्ययन संसाधन", "مطالعاتی وسائل"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.resources.forEach { res ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable {
                                val url = res.file ?: res.link
                                if (!url.isNullOrBlank()) {
                                    val finalUrl = if (url.startsWith("http")) url else "http://10.0.2.2:8000$url"
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl)))
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color(0xFF1976D2))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(res.title, fontWeight = FontWeight.Medium, color = Color(0xFF1565C0))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

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
private fun DiplomaSection(title: String, enContent: String?, transContent: String?) {
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