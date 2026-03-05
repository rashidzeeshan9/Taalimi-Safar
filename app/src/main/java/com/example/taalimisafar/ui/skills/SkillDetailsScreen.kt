package com.example.taalimisafar.ui.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.SkillModule
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.SkillViewModel
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailScreen(
    programId: Int,
    viewModel: SkillViewModel,
    onBackClick: () -> Unit,
    onEnrollClick: (String) -> Unit
) {
    val program by viewModel.selectedProgram.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

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

    fun dualLabel(en: String, hi: String, ur: String): String {
        return when (language) {
            AppLanguage.HINDI -> "$en / $hi"
            AppLanguage.URDU -> "$en / $ur"
            else -> en
        }
    }

    val lblProgramDetails = dualLabel("Program Details", "कार्यक्रम का विवरण", "پروگرام کی تفصیلات")
    val lblMode = dualLabel("Mode", "मोड", "موڈ")
    val lblLevel = dualLabel("Level", "स्तर", "سطح")
    val lblDuration = dualLabel("Duration", "अवधि", "مدت")
    val lblFees = dualLabel("Fees", "फीस", "فیس")
    val lblOverview = dualLabel("Overview", "अवलोकन", "جائزہ")
    val lblEligibility = dualLabel("Eligibility", "पात्रता", "اہلیت")
    val lblSyllabus = dualLabel("Syllabus", "पाठ्यक्रम", "نصاب")
    val lblEnroll = dualLabel("Enroll Now", "अभी नामांकन करें", "ابھی داخلہ لیں")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lblProgramDetails, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4F46E5))
            )
        },
        bottomBar = {
            if (!p.program_link.isNullOrBlank()) {
                Surface(color = Color.White, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onEnrollClick(p.program_link) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
                    ) {
                        Text(lblEnroll, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(85.dp).clip(CircleShape).background(Color(0xFFEEF2FF)), contentAlignment = Alignment.Center) {
                        if (!p.image.isNullOrEmpty() && p.image != "null") {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(if (p.image.startsWith("http")) p.image else "http://10.0.2.2:8000" + p.image)
                                    .build(),
                                contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(45.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = p.name, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B), textAlign = TextAlign.Center)
                    val transTitle = when(language) { AppLanguage.HINDI -> p.name_hi; AppLanguage.URDU -> p.name_ur; else -> null }
                    if (!transTitle.isNullOrBlank() && transTitle != p.name) {
                        Text(text = transTitle, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!p.provider_name.isNullOrBlank()) {
                        Text(text = p.provider_name, fontSize = 16.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
                        val transOrg = when(language) { AppLanguage.HINDI -> p.provider_name_hi; AppLanguage.URDU -> p.provider_name_ur; else -> null }
                        if (!transOrg.isNullOrBlank() && transOrg != p.provider_name) {
                            Text(text = transOrg, fontSize = 15.sp, color = Color(0xFF4F46E5), textAlign = TextAlign.Center)
                        }
                    }

                    if (p.placement_support) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) {
                            Text(text = "PLACEMENT SUPPORT", modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SkillLineByLineInfoBox(lblMode, p.mode.uppercase(), null, Modifier.weight(1f))
                SkillLineByLineInfoBox(lblLevel, p.level.uppercase(), null, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val durationTrans = when(language){ AppLanguage.HINDI -> p.duration_hi; AppLanguage.URDU -> p.duration_ur; else -> null }
                val feeText = if (p.fees.isNullOrBlank() || p.fees == "0.00") "Free" else "₹${p.fees}"

                SkillLineByLineInfoBox(lblDuration, p.duration, durationTrans, Modifier.weight(1f))
                SkillLineByLineInfoBox(lblFees, feeText, null, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text Blocks
            SkillInterleavedDetailSection(lblOverview, p.overview, when(language){ AppLanguage.HINDI -> p.overview_hi; AppLanguage.URDU -> p.overview_ur; else -> null })

            Spacer(modifier = Modifier.height(16.dp))

            if (!p.eligibility.isNullOrBlank()) {
                SkillInterleavedDetailSection(lblEligibility, p.eligibility, when(language){ AppLanguage.HINDI -> p.eligibility_hi; AppLanguage.URDU -> p.eligibility_ur; else -> null })
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Modules Loop
            if (p.modules.isNotEmpty()) {
                Text(text = lblSyllabus, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))

                p.modules.forEach { module ->
                    ModuleInterleavedCard(module, language)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun SkillLineByLineInfoBox(title: String, enValue: String, transValue: String?, modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(1.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = enValue, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            if (!transValue.isNullOrBlank() && transValue != enValue) {
                Text(text = transValue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
            }
        }
    }
}

@Composable
private fun SkillInterleavedDetailSection(title: String, enContent: String?, transContent: String?) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    Spacer(modifier = Modifier.height(8.dp))
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (enContent.isNullOrBlank()) {
                Text("Not Specified", fontSize = 14.sp, color = Color.LightGray)
                return@Column
            }
            val enLines = enContent.split(Regex("[\n.]")).map { it.trim() }.filter { it.isNotEmpty() }
            val transLines = transContent?.split(Regex("[\n।.]"))?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            val maxLines = maxOf(enLines.size, transLines.size)
            for (i in 0 until maxLines) {
                val enLine = enLines.getOrNull(i)
                val transLine = transLines.getOrNull(i)
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    if (enLine != null) Text(text = "• $enLine", fontSize = 14.sp, color = Color(0xFF334155), lineHeight = 20.sp)
                    if (transLine != null && transLine != enLine) Text(text = "  $transLine", fontSize = 14.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Medium, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun ModuleInterleavedCard(module: SkillModule, language: AppLanguage) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = module.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            val titleTrans = when(language) { AppLanguage.HINDI -> module.title_hi; AppLanguage.URDU -> module.title_ur; else -> null }
            if (!titleTrans.isNullOrBlank() && titleTrans != module.title) {
                Text(text = titleTrans, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
            }
            if (!module.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                val descTrans = when(language) { AppLanguage.HINDI -> module.description_hi; AppLanguage.URDU -> module.description_ur; else -> null }
                Text(text = module.description, fontSize = 14.sp, color = Color(0xFF64748B), lineHeight = 20.sp)
                if (!descTrans.isNullOrBlank() && descTrans != module.description) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = descTrans, fontSize = 14.sp, color = Color(0xFF4F46E5), lineHeight = 20.sp)
                }
            }
        }
    }
}