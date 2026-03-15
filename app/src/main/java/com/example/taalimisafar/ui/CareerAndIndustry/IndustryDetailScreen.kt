package com.example.taalimisafar.ui.CareerAndIndustry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.IndustryProgram
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.utils.AppLanguage

private val IndigoPrimary = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFFE0E7FF)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryDetailScreen(program: IndustryProgram?, onBackClick: () -> Unit) {
    val currentLanguage = LanguageManager.currentLanguage.value

    if (program == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = IndigoPrimary)
        }
        return
    }

    fun getTrans(hi: String?, ur: String?): String? = when (currentLanguage) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    fun getInlineText(en: String, hi: String?, ur: String?): String {
        val trans = getTrans(hi, ur)
        return if (!trans.isNullOrBlank() && trans != "null") "$en • $trans" else en
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(padding).verticalScroll(rememberScrollState())) {

            // --- HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = IndigoPrimary,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(top = 8.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                Column {
                    DualLineText(
                        enText = program.title,
                        transText = getTrans(program.title_hi, program.title_ur),
                        isHeader = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(program.average_salary ?: "Variable", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.width(16.dp))
                            Text("|", color = Color.White.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(program.duration ?: "Varies", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // --- MAIN CONTENT ---
            Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-30).dp)) {

                // 1. OVERVIEW
                if (!program.overview.isNullOrBlank()) {
                    val overviewTitle = getInlineText("Overview", "अवलोकन", "جائزہ")
                    InteractiveExpandable(icon = Icons.Default.Info, title = overviewTitle, isInitiallyExpanded = true) {
                        LineByLineDualText(
                            enText = program.overview,
                            transText = getTrans(program.overview_hi, program.overview_ur)
                        )
                    }
                }

                // 2. REQUIRED SKILLS
                if (program.skills.isNotEmpty()) {
                    val skillsTitle = getInlineText("Required Skills", "आवश्यक कौशल", "مطلوبہ مہارتیں")
                    InteractiveExpandable(icon = Icons.Default.Psychology, title = skillsTitle, isInitiallyExpanded = true) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            program.skills.forEach { skill ->
                                Row(verticalAlignment = Alignment.Top) {
                                    // Small bullet point icon
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    DualLineText(
                                        enText = skill.name,
                                        transText = getTrans(skill.name_hi, skill.name_ur),
                                        isDescription = false
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. CAREER ROADMAP
                if (program.roadmaps.isNotEmpty()) {
                    val roadmapTitle = getInlineText("Career Roadmap", "करियर रोडमैप", "کیریئر روڈ میپ")
                    InteractiveExpandable(icon = Icons.Default.Route, title = roadmapTitle, isInitiallyExpanded = true) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            program.roadmaps.sortedBy { it.step_number }.forEach { step ->
                                Row(verticalAlignment = Alignment.Top) {
                                    // Blue Rounded Square matching your screenshot
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = IndigoPrimary,
                                        modifier = Modifier.size(32.dp).padding(top = 2.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("${step.step_number}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        DualLineText(
                                            enText = step.title,
                                            transText = getTrans(step.title_hi, step.title_ur),
                                            isDescription = false
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        LineByLineDualText(
                                            enText = step.description,
                                            transText = getTrans(step.description_hi, step.description_ur)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. FUTURE SCOPE
                if (!program.future_scope.isNullOrBlank()) {
                    val scopeTitle = getInlineText("Future Scope", "भविष्य की संभावनाएं", "مستقبل کا دائرہ کار")
                    InteractiveExpandable(icon = Icons.Default.TrendingUp, title = scopeTitle) {
                        LineByLineDualText(
                            enText = program.future_scope,
                            transText = getTrans(program.future_scope_hi, program.future_scope_ur)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
@Composable
fun DualLineText(enText: String?, transText: String?, isHeader: Boolean = false, isDescription: Boolean = false) {
    Column {
        // English Line
        if (!enText.isNullOrBlank() && enText != "null") {
            Text(
                text = enText,
                fontSize = if(isHeader) 26.sp else if(isDescription) 14.sp else 16.sp,
                fontWeight = if(isHeader) FontWeight.ExtraBold else if(isDescription) FontWeight.Normal else FontWeight.Bold,
                color = if(isHeader) Color.White else if(isDescription) TextMuted else TextDark,
                lineHeight = if(isHeader) 32.sp else 22.sp
            )
        }
        // Translated Line
        if (!transText.isNullOrBlank() && transText != "null") {
            Text(
                text = transText,
                fontSize = if(isHeader) 18.sp else if(isDescription) 13.sp else 14.sp,
                color = if(isHeader) Color.White.copy(alpha=0.8f) else IndigoPrimary,
                fontWeight = if(isHeader) FontWeight.Bold else FontWeight.Medium,
                lineHeight = if(isHeader) 24.sp else 20.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun LineByLineDualText(enText: String?, transText: String?) {
    // Failsafe: If English is missing but translation exists, just show the translation
    if (enText.isNullOrBlank() || enText == "null") {
        if (!transText.isNullOrBlank() && transText != "null") {
            Text(text = transText, fontSize = 14.sp, color = IndigoPrimary, lineHeight = 22.sp)
        }
        return
    }

    val enLines = enText.split("\n").filter { it.isNotBlank() }
    val transLines = transText?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()

    Column {
        enLines.forEachIndexed { index, enLine ->
            val transLine = transLines.getOrNull(index)

            // English Line
            Text(
                text = enLine.trim(),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 22.sp
            )

            // Translated Line
            if (!transLine.isNullOrBlank() && transLine != "null") {
                Text(
                    text = transLine.trim(),
                    fontSize = 14.sp,
                    color = IndigoPrimary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun InteractiveExpandable(icon: ImageVector, title: String, isInitiallyExpanded: Boolean = false, content: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(isInitiallyExpanded) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "ArrowRotation")
    val bgColor by animateColorAsState(targetValue = if (isExpanded) Color(0xFFFAFAFF) else Color.White, label = "BgColor")

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(RoundedCornerShape(16.dp)).clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(IndigoLight, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 16.sp, modifier = Modifier.weight(1f), lineHeight = 22.sp)
                Icon(Icons.Default.ExpandMore, null, tint = TextMuted, modifier = Modifier.rotate(rotationAngle))
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}