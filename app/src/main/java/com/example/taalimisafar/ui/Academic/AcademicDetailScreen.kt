package com.example.taalimisafar.ui.academic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.AcademicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicDetailScreen(
    courseId: Int,
    viewModel: AcademicViewModel,
    onBackClick: () -> Unit
) {

    val course by viewModel.selectedCourse.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

    LaunchedEffect(courseId) {
        viewModel.fetchCourseDetail(courseId)
    }

    if (isLoading || course == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF4F46E5))
        }
        return
    }

    val c: Course = course ?: return

    fun formatDualTitle(en: String, hi: String, ur: String): String {
        val trans = when (language) {
            AppLanguage.HINDI -> hi
            AppLanguage.URDU -> ur
            else -> null
        }
        return if (!trans.isNullOrBlank() && trans != en) "$en / $trans" else en
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        formatDualTitle(
                            "Course Details",
                            "कोर्स विवरण",
                            "کورس کی تفصیلات"
                        ),
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5)
                )
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

            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFEEF2FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Course Title
            Text(
                text = c.courseName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            val titleTrans = when (language) {
                AppLanguage.HINDI -> c.courseName_hi
                AppLanguage.URDU -> c.courseName_ur
                else -> null
            }

            if (!titleTrans.isNullOrBlank() && titleTrans != c.courseName) {
                Text(
                    text = titleTrans,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4F46E5)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoChip("Duration", c.duration)
                InfoChip("Salary", c.average_salary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AcademicSection(
                formatDualTitle("Description", "विवरण", "تفصیل"),
                c.description,
                when (language) {
                    AppLanguage.HINDI -> c.description_hi
                    AppLanguage.URDU -> c.description_ur
                    else -> null
                }
            )

            AcademicSection(
                formatDualTitle("Eligibility", "पात्रता", "اہلیت"),
                c.eligibility,
                when (language) {
                    AppLanguage.HINDI -> c.eligibility_hi
                    AppLanguage.URDU -> c.eligibility_ur
                    else -> null
                }
            )

            AcademicSection(
                formatDualTitle("Admission Process", "प्रवेश प्रक्रिया", "داخلہ کا عمل"),
                c.admission_process,
                when (language) {
                    AppLanguage.HINDI -> c.admission_process_hi
                    AppLanguage.URDU -> c.admission_process_ur
                    else -> null
                }
            )

            AcademicSection(
                formatDualTitle("Future Scope", "भविष्य की संभावनाएं", "مستقبل کے مواقع"),
                c.future_scope,
                when (language) {
                    AppLanguage.HINDI -> c.future_scope_hi
                    AppLanguage.URDU -> c.future_scope_ur
                    else -> null
                }
            )

            // Curriculum (single text block from API)
            if (c.curriculum.isNotEmpty()) {
                AcademicSection(
                    formatDualTitle("Curriculum", "पाठ्यक्रम", "نصاب"),
                    c.curriculum,
                    when (language) {
                        AppLanguage.HINDI -> c.curriculum_hi
                        AppLanguage.URDU -> c.curriculum_ur
                        else -> null
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

        }

    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }
    }
}

@Composable
private fun AcademicSection(title: String, enContent: String?, transContent: String?) {
    if (enContent.isNullOrBlank()) return
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(enContent, fontSize = 14.sp, color = Color(0xFF334155))
            if (!transContent.isNullOrBlank() && transContent != enContent) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(transContent, fontSize = 14.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Medium)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}