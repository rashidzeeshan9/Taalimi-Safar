package com.example.taalimisafar.ui.courses

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.ui.components.DualLangText
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.AppStrings
import com.example.taalimisafar.utils.LanguageManager

@Composable
fun CourseItem(course: Course) {
    val currentLanguage = LanguageManager.currentLanguage.value
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .animateContentSize()
    ) {
        // --- 1. HEADER CARD ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFE8EAF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.School, null, tint = Color(0xFF1A237E), modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                DualLangText(
                    english = course.courseName,
                    hindi = course.courseName_hi,
                    urdu = course.courseName_ur,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. HIGHLIGHTS ---
        Text(
            text = "Key Highlights",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HighlightCard(Icons.Default.Schedule, "Duration", course.duration, course.duration_hi, course.duration_ur, Color(0xFFFFF3E0), Color(0xFFEF6C00), Modifier.weight(1f))
            HighlightCard(Icons.Default.Verified, "Eligibility", course.eligibility, course.eligibility_hi, course.eligibility_ur, Color(0xFFE3F2FD), Color(0xFF1565C0), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HighlightCard(Icons.Default.Laptop, "Mode", course.application_mode, course.application_mode_hi, course.application_mode_ur, Color(0xFFE8F5E9), Color(0xFF2E7D32), Modifier.weight(1f))
            HighlightCard(Icons.Default.School, "Qualification", course.qualification, course.qualification_hi, course.qualification_ur, Color(0xFFF3E5F5), Color(0xFF8E24AA), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3. FINANCIAL ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, null, tint = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.width(8.dp))

                    // FIXED: Financial Header in Dual Language
                    val finLabel = "Financial Overview"
                    val finTrans = AppStrings.getLabel(finLabel, currentLanguage)
                    Text(
                        text = if(finTrans != finLabel) "$finLabel / $finTrans" else finLabel,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    FinancialItem("Govt Fees", "₹${course.average_course_fees_govt}", Icons.Default.AccountBalance, Modifier.weight(1f))
                    FinancialItem("Pvt Fees", "₹${course.average_course_fees_pvt}", Icons.Default.Business, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                FinancialItem("Avg Salary", "₹${course.average_salary}", Icons.Default.Payments, Modifier.fillMaxWidth())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 4. EXPANDABLE DETAILS ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expanded) "Hide Details" else "View Course Details",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )
                    Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF1A237E))
                }

                if (expanded) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    InfoSection("Admission Process", course.admission_process, course.admission_process_hi, course.admission_process_ur)
                    InfoSection("Admission Date", course.admission_date, course.admission_date_hi, course.admission_date_ur)
                    InfoSection("Course Overview", course.description, course.description_hi, course.description_ur)
                    InfoSection("Curriculum", course.curriculum, course.curriculum_hi, course.curriculum_ur)
                    InfoSection("Future Scope", course.future_scope, course.future_scope_hi, course.future_scope_ur)
                }
            }
        }
    }
}

// --- HELPER: InfoSection (FIXED: Shows "English / Hindi" in Title) ---
@Composable
fun InfoSection(title: String, eng: String, hi: String?, ur: String?) {
    val currentLanguage = LanguageManager.currentLanguage.value

    Column(modifier = Modifier.padding(vertical = 12.dp)) {

        // 1. DETERMINE TITLE (English / Translated)
        val translatedTitle = AppStrings.getLabel(title, currentLanguage)
        val displayTitle = if (currentLanguage != AppLanguage.NONE && translatedTitle != title) {
            "$title / $translatedTitle"
        } else {
            title
        }

        // 2. SHOW TITLE
        Text(
            text = displayTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 3. SHOW CONTENT
        val isLongText = title.contains("Overview", ignoreCase = true) ||
                title.contains("Description", ignoreCase = true) ||
                title.contains("Scope", ignoreCase = true) ||
                title.contains("Curriculum", ignoreCase = true) ||
                title.contains("Process", ignoreCase = true)

        if (isLongText && currentLanguage != AppLanguage.NONE) {
            val nativeText = if (currentLanguage == AppLanguage.HINDI) hi else ur
            InterleavedDescription(eng, nativeText, currentLanguage)
        } else {
            DualLangText(
                english = eng,
                hindi = hi,
                urdu = ur,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF455A64)
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
    }
}

// --- HELPER: InterleavedDescription ---
@Composable
fun InterleavedDescription(english: String, translated: String?, language: AppLanguage) {
    if (translated.isNullOrBlank()) {
        Text(text = english, color = Color.DarkGray, style = MaterialTheme.typography.bodyMedium)
        return
    }

    val engSentences = english.split(".").map { it.trim() }.filter { it.isNotEmpty() }
    val transDelimiters = if (language == AppLanguage.HINDI) charArrayOf('|', '.', '।') else charArrayOf('۔', '.')
    val transSentences = translated.split(*transDelimiters).map { it.trim() }.filter { it.isNotEmpty() }

    val maxLines = maxOf(engSentences.size, transSentences.size)
    val isUrdu = (language == AppLanguage.URDU)

    Column {
        for (i in 0 until maxLines) {
            if (i < engSentences.size) {
                Text(
                    text = engSentences[i] + ".",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )
            }
            if (i < transSentences.size) {
                val endChar = if (isUrdu) "۔" else " ।"
                Text(
                    text = transSentences[i] + endChar,
                    color = if (isUrdu) Color(0xFF00695C) else Color(0xFF283593),
                    textAlign = if (isUrdu) TextAlign.End else TextAlign.Start,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }
        }
    }
}

// --- HELPER: HighlightCard ---
@Composable
fun HighlightCard(
    icon: ImageVector,
    label: String,
    valueEn: String,
    valueHi: String?,
    valueUr: String?,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                InfoLabel(label)
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (valueEn.isNotBlank()) {
                Text(valueEn, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            if (currentLanguage != AppLanguage.NONE) {
                val translated = if (currentLanguage == AppLanguage.HINDI) valueHi else valueUr
                if (!translated.isNullOrBlank() && translated != valueEn) {
                    Text(
                        text = translated,
                        color = if (currentLanguage == AppLanguage.URDU) Color(0xFF00695C) else Color(0xFF283593),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                } else if (translated.isNullOrBlank()) {
                    Text("-", color = Color.LightGray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun InfoLabel(label: String) {
    val currentLanguage = LanguageManager.currentLanguage.value
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 12.sp)
        if (currentLanguage != AppLanguage.NONE) {
            val translated = AppStrings.getLabel(label, currentLanguage)
            if (translated != label) {
                Text(" / $translated", style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun FinancialItem(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            InfoLabel(label)
            Text(value, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 15.sp)
        }
    }
}