package com.example.taalimisafar.ui.religious

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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayCircle
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
import com.example.taalimisafar.viewmodel.ReligiousViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReligiousDetailScreen(
    programId: Int,
    viewModel: ReligiousViewModel,
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

    fun formatDualTitle(en: String, hi: String?, ur: String?): String {
        val trans = when (language) {
            AppLanguage.HINDI -> hi
            AppLanguage.URDU -> ur
            else -> null
        }
        return if (!trans.isNullOrBlank() && trans != en) "$en / $trans" else en
    }
    val appBarTitle = formatDualTitle(p.program_name, p.program_name_hi, p.program_name_ur)
    val lblIntro = formatDualTitle("Introduction", "परिचय", "تعارف")
    val lblFaq = formatDualTitle("Frequently Asked Questions", "अक्सर पूछे जाने वाले प्रश्न", "اکثر پوچھے گئے سوالات")
    val lblVideos = formatDualTitle("Related Videos", "संबंधित वीडियो", "متعلقہ ویڈیوز")
    val lblRefs = formatDualTitle("References", "संदर्भ", "حوالہ جات")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(appBarTitle, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- MAIN IMAGE ---
            if (!p.image.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (p.image.startsWith("http")) p.image else "http://10.0.2.2:8000" + p.image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- INTRODUCTION ---
            ReligiousInterleavedSection(
                title = lblIntro,
                enContent = p.description,
                transContent = when(language) { AppLanguage.HINDI -> p.description_hi; AppLanguage.URDU -> p.description_ur; else -> null }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- DYNAMIC SECTIONS ---
            p.sections.forEach { section ->
                val dualSecTitle = formatDualTitle(section.title, section.title_hi, section.title_ur)
                ReligiousInterleavedSection(
                    title = dualSecTitle,
                    enContent = section.content,
                    transContent = when(language) { AppLanguage.HINDI -> section.content_hi; AppLanguage.URDU -> section.content_ur; else -> null }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- FAQS ---
            if (p.faqs.isNotEmpty()) {
                Text(lblFaq, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.faqs.forEach { faq ->
                    val dualQ = formatDualTitle("Q: ${faq.question}", "प्र: ${faq.question_hi}", "سوال: ${faq.question_ur}")
                    ReligiousInterleavedSection(
                        title = dualQ,
                        enContent = faq.answer,
                        transContent = when(language) { AppLanguage.HINDI -> faq.answer_hi; AppLanguage.URDU -> faq.answer_ur; else -> null }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- VIDEOS ---
            if (p.videos.isNotEmpty()) {
                Text(lblVideos, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.videos.forEach { video ->
                    val dualVidTitle = formatDualTitle(video.title, video.title_hi, video.title_ur)
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.youtube_link)))
                        },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Play", tint = Color.Red, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = dualVidTitle, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- REFERENCES ---
            if (p.references.isNotEmpty()) {
                Text(lblRefs, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                p.references.forEach { ref ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ref.reference_link)))
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = "Link", tint = Color(0xFF4F46E5), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = ref.source_name ?: "External Source",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
@Composable
private fun ReligiousInterleavedSection(title: String, enContent: String?, transContent: String?) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val enLines = enContent?.split(Regex("[\n.]"))?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            val transLines = transContent?.split(Regex("[\n।.]"))?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            val maxLines = maxOf(enLines.size, transLines.size)

            for (i in 0 until maxLines) {
                val enLine = enLines.getOrNull(i)
                val transLine = transLines.getOrNull(i)
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    if (enLine != null) Text(text = enLine, fontSize = 15.sp, color = Color(0xFF334155), lineHeight = 22.sp)
                    if (transLine != null && transLine != enLine) {
                        Text(text = transLine, fontSize = 15.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Medium, lineHeight = 22.sp)
                    }
                }
            }
        }
    }
}