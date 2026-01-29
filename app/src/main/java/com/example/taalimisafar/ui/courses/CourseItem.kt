package com.example.taalimisafar.ui.courses

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.ui.components.DualLangText

@Composable
fun CourseItem(course: Course) {
    // State to toggle "Read More" expansion
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .animateContentSize(), // Smooth animation when expanding
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- 1. HEADER (Course Name) ---
            DualLangText(
                english = course.courseName,
                hindi = course.courseName_hi,
                urdu = course.courseName_ur,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // --- 2. KEY HIGHLIGHTS (Grid Layout) ---
            Row(modifier = Modifier.fillMaxWidth()) {
                // Column 1
                Column(modifier = Modifier.weight(1f)) {
                    InfoLabel("Duration")
                    DualLangText(course.duration, course.duration_hi, course.duration_ur)

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoLabel("Eligibility")
                    DualLangText(course.eligibility, course.eligibility_hi, course.eligibility_ur)

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoLabel("Age Limit")
                    DualLangText(course.age_limit, course.age_limit_hi, course.age_limit_ur)
                }

                // Column 2
                Column(modifier = Modifier.weight(1f)) {
                    InfoLabel("12th % Req")
                    DualLangText(course.percentage_12th, course.percentage_12th_hi, course.percentage_12th_ur)

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoLabel("Qualification")
                    DualLangText(course.qualification, course.qualification_hi, course.qualification_ur)

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoLabel("Mode")
                    DualLangText(course.application_mode, course.application_mode_hi, course.application_mode_ur)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. FINANCIALS (Green Box) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Financial Overview", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            InfoLabel("Govt Fees")
                            DualLangText(course.average_course_fees_govt, course.average_course_fees_govt_hi, course.average_course_fees_govt_ur, color = Color(0xFF1B5E20))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            InfoLabel("Pvt Fees")
                            DualLangText(course.average_course_fees_pvt, course.average_course_fees_pvt_hi, course.average_course_fees_pvt_ur, color = Color(0xFF1B5E20))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoLabel("Avg Salary")
                    DualLangText(course.average_salary, course.average_salary_hi, course.average_salary_ur, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 4. EXPANDABLE DETAILS (See More) ---
            if (expanded) {
                InfoSection("Admission Process", course.admission_process, course.admission_process_hi, course.admission_process_ur)
                InfoSection("Admission Date", course.admission_date, course.admission_date_hi, course.admission_date_ur)
                InfoSection("Course Overview", course.description, course.description_hi, course.description_ur)
                InfoSection("Curriculum", course.curriculum, course.curriculum_hi, course.curriculum_ur)
                InfoSection("Future Scope", course.future_scope, course.future_scope_hi, course.future_scope_ur)
            }

            // Expand/Collapse Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "Show Less" else "View Full Details",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Helper 1: Small Label Text
@Composable
fun InfoLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        fontSize = 10.sp
    )
}

// Helper 2: Full Section Block
@Composable
fun InfoSection(title: String, eng: String, hi: String?, ur: String?) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        DualLangText(
            english = eng,
            hindi = hi,
            urdu = ur,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
        Divider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(top = 8.dp))
    }
}