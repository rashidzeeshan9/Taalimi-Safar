package com.example.taalimisafar.ui.jobs

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
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
import com.example.taalimisafar.data.model.Job
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    jobId: Int,
    viewModel: JobViewModel,
    onBackClick: () -> Unit
) {
    val jobs by viewModel.jobs.collectAsState()
    val job = jobs.find { it.id == jobId }
    val language = LanguageManager.currentLanguage.value
    val context = LocalContext.current

    if (job == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF4F46E5))
        }
        return
    }

    // --- Labels (Interleaved Titles) ---
    fun dualLabel(en: String, hi: String, ur: String): String {
        return when (language) {
            AppLanguage.HINDI -> "$en / $hi"
            AppLanguage.URDU -> "$en / $ur"
            else -> en
        }
    }

    val lblJobDetails = dualLabel("Job Details", "नौकरी का विवरण", "نوکری کی تفصیلات")
    val lblSalary = dualLabel("Salary", "वेतन", "تنخواہ")
    val lblExperience = dualLabel("Experience", "अनुभव", "تجربہ")
    val lblLocation = dualLabel("Location", "स्थान", "مقام")
    val lblVacancies = dualLabel("Vacancies", "रिक्तियां", "آسامیاں")
    val lblDesc = dualLabel("Job Description", "नौकरी का विवरण", "نوکری کی تفصیل")
    val lblSkills = dualLabel("Skills Required", "आवश्यक कौशल", "مطلوبہ مہارتیں")
    val lblResp = dualLabel("Responsibilities", "जिम्मेदारियां", "ذمہ داریاں")
    val lblApply = dualLabel("Apply Now", "अभी आवेदन करें", "ابھی اپلائی کریں")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lblJobDetails, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4F46E5))
            )
        },
        bottomBar = {
            Surface(color = Color.White, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val link = job.apply_link_or_email
                        if (!link.isNullOrBlank()) {
                            try {
                                val url = if (!link.startsWith("http")) "https://$link" else link
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open link.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
                ) {
                    Text(lblApply, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
            // --- HEADER CARD: Centered Logo & Names ---
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
                    // Logo with Default Icon logic
                    Box(modifier = Modifier.size(85.dp).clip(CircleShape).background(Color(0xFFEEF2FF)), contentAlignment = Alignment.Center) {
                        if (!job.organization_logo.isNullOrEmpty() && job.organization_logo != "null") {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(if (job.organization_logo.startsWith("http")) job.organization_logo else "http://10.0.2.2:8000" + job.organization_logo)
                                    .build(),
                                contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(45.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Centered Job Title
                    Text(text = job.title ?: "", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B), textAlign = TextAlign.Center)
                    val transTitle = when(language) { AppLanguage.HINDI -> job.title_hi; AppLanguage.URDU -> job.title_ur; else -> null }
                    if (!transTitle.isNullOrBlank()) {
                        Text(text = transTitle, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Centered Organization Name
                    Text(text = job.organization_name ?: "", fontSize = 16.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
                    val transOrg = when(language) { AppLanguage.HINDI -> job.organization_name_hi; AppLanguage.URDU -> job.organization_name_ur; else -> null }
                    if (!transOrg.isNullOrBlank()) {
                        Text(text = transOrg, fontSize = 15.sp, color = Color(0xFF4F46E5), textAlign = TextAlign.Center)
                    }

                    // Job Type Badge
                    if (!job.job_type.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = job.job_type.uppercase(),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- INFO GRID: Salary, Experience, Location, Vacancy ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LineByLineInfoBox(lblSalary, job.salary_or_payscale ?: "N/A", when(language){ AppLanguage.HINDI -> job.salary_or_payscale_hi; AppLanguage.URDU -> job.salary_or_payscale_ur; else -> null }, Modifier.weight(1f))
                LineByLineInfoBox(lblExperience, job.experience_required ?: "N/A", when(language){ AppLanguage.HINDI -> job.experience_required_hi; AppLanguage.URDU -> job.experience_required_ur; else -> null }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Correct Location logic matching List Screen (City/Location focus)
                val locText = when {
                    job.is_remote -> "Remote"
                    !job.location.isNullOrBlank() -> job.location
                    else -> job.location_state_name ?: "India"
                }
                val transLoc = when {
                    job.is_remote -> if (language == AppLanguage.HINDI) "रिमोट" else "ریموٹ"
                    language == AppLanguage.HINDI -> job.location_hi
                    language == AppLanguage.URDU -> job.location_ur
                    else -> null
                }
                LineByLineInfoBox(lblLocation, locText, transLoc, Modifier.weight(1f))
                LineByLineInfoBox(lblVacancies, job.total_vacancies?.toString() ?: "N/A", null, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INTERLEAVED LONG TEXT SECTIONS (Regex Splitting) ---
            InterleavedDetailSection(lblDesc, job.job_description, when(language){ AppLanguage.HINDI -> job.job_description_hi; AppLanguage.URDU -> job.job_description_ur; else -> null }, language)
            Spacer(modifier = Modifier.height(16.dp))
            InterleavedDetailSection(lblSkills, job.skills_required, when(language){ AppLanguage.HINDI -> job.skills_required_hi; AppLanguage.URDU -> job.skills_required_ur; else -> null }, language)
            Spacer(modifier = Modifier.height(16.dp))
            InterleavedDetailSection(lblResp, job.responsibilities, when(language){ AppLanguage.HINDI -> job.responsibilities_hi; AppLanguage.URDU -> job.responsibilities_ur; else -> null }, language)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun LineByLineInfoBox(title: String, enValue: String, transValue: String?, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
    ) {
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
fun InterleavedDetailSection(title: String, enContent: String?, transContent: String?, language: AppLanguage) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (enContent.isNullOrBlank()) {
                Text("Not Specified", fontSize = 14.sp, color = Color.LightGray)
                return@Column
            }

            // Splitting logic using Regex for Newlines, English periods, and Hindi Purna Virams
            val enLines = enContent.split(Regex("[\n.]")).map { it.trim() }.filter { it.isNotEmpty() }
            val transLines = transContent?.split(Regex("[\n।.]"))?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

            val maxLines = maxOf(enLines.size, transLines.size)

            for (i in 0 until maxLines) {
                val enLine = enLines.getOrNull(i)
                val transLine = transLines.getOrNull(i)

                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    if (enLine != null) {
                        Text(text = "• $enLine", fontSize = 14.sp, color = Color(0xFF334155), lineHeight = 20.sp)
                    }
                    if (transLine != null && transLine != enLine) {
                        Text(
                            text = "  $transLine",
                            fontSize = 14.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Medium, lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}