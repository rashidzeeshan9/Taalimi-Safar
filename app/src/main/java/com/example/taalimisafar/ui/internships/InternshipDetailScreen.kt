package com.example.taalimisafar.ui.internships

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.AppStrings
import com.example.taalimisafar.viewmodel.InternshipViewModel

// Make sure these match your theme colors, or replace them with your actual color references
private val IndigoPrimary = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFFE0E7FF)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipDetailScreen(
    internshipId: Int,
    viewModel: InternshipViewModel,
    onBackClick: () -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val internships by viewModel.internships.collectAsState()
    val internship = internships.find { it.id == internshipId }
    val context = LocalContext.current

    if (internship == null) {
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
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        },
        bottomBar = {
            Surface(color = Color.White, shadowElevation = 24.dp, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            val url = internship.application_link_or_email ?: ""
                            if (url.isNotBlank()) {
                                try {
                                    val intentUri = if (url.contains("@") && !url.startsWith("http")) Uri.parse("mailto:$url") else Uri.parse(url)
                                    context.startActivity(Intent(Intent.ACTION_VIEW, intentUri))
                                } catch (e: Exception) {}
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apply Now", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(paddingValues).verticalScroll(rememberScrollState())) {

            // HEADER
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
                        enText = internship.title,
                        transText = getTrans(internship.title_hi, internship.title_ur),
                        isHeader = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val orgDomainEn = "${internship.organization_name} • ${internship.domain}"
                    val orgDomainTrans = getTrans(
                        "${internship.organization_name_hi} • ${internship.domain_hi}",
                        "${internship.organization_name_ur} • ${internship.domain_ur}"
                    )

                    Surface(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
                        Text(
                            text = if (!orgDomainTrans.isNullOrBlank()) "$orgDomainEn\n$orgDomainTrans" else orgDomainEn,
                            color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // MAIN CONTENT
            Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-30).dp)) {

                // DASHBOARD CARD
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // IMAGE OR DEFAULT LOGO LOGIC
                            Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = IndigoLight) {
                                val imageUrl = internship.image?.let { img ->
                                    if (img.startsWith("http")) img else "http://10.0.2.2:8000$img"
                                }

                                if (!imageUrl.isNullOrBlank() && imageUrl != "null") {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Company Logo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    // Default Fallback Logo
                                    Icon(Icons.Default.Business, contentDescription = "Default Logo", modifier = Modifier.padding(16.dp), tint = IndigoPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Mode", fontSize = 12.sp, color = TextMuted)
                                Surface(color = IndigoLight, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                                    Text(text = internship.mode.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF1F5F9))

                        // Grid Row 1
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoBox(icon = Icons.Default.LocationOn, iconBg = Color(0xFFE0F2FE), iconColor = Color(0xFF0284C7), title = "Location", valueEn = internship.location, valueTrans = getTrans(internship.location_hi, internship.location_ur), modifier = Modifier.weight(1f))
                            InfoBox(icon = Icons.Default.Timer, iconBg = Color(0xFFFEF3C7), iconColor = Color(0xFFD97706), title = AppStrings.getLabel("Duration", currentLanguage), valueEn = internship.duration, valueTrans = getTrans(internship.duration_hi, internship.duration_ur), modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid Row 2
                        val stipend = if (internship.stipend_amount.isNullOrBlank() || internship.stipend_amount == "0" || internship.stipend_amount == "0.00") "Unpaid" else "₹${internship.stipend_amount}"
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoBox(icon = Icons.Default.Payments, iconBg = Color(0xFFD1FAE5), iconColor = Color(0xFF059669), title = "Stipend", valueEn = stipend, valueTrans = null, modifier = Modifier.weight(1f))
                            InfoBox(icon = Icons.Default.Event, iconBg = Color(0xFFFFE4E6), iconColor = Color(0xFFE11D48), title = "Apply By", valueEn = internship.last_date_to_apply ?: "N/A", valueTrans = null, modifier = Modifier.weight(1f))
                        }
                    }
                }

                // RESPONSIBILITIES
                val respTitle = getInlineText("Responsibilities", "जिम्मेदारियां", "ذمہ داریاں")
                InteractiveExpandable(icon = Icons.Default.Description, title = respTitle, isInitiallyExpanded = true) {
                    LineByLineDualText(
                        enText = internship.responsibilities,
                        transText = getTrans(internship.responsibilities_hi, internship.responsibilities_ur)
                    )
                }

                // SKILLS & TOOLS
                val skillsTitle = getInlineText("Skills & Tools", "कौशल और उपकरण", "مہارت اور ٹولز")
                InteractiveExpandable(icon = Icons.Default.Handyman, title = skillsTitle) {
                    if (!internship.skills_required.isNullOrBlank() && internship.skills_required != "null") {
                        Text("Tech Stack:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = IndigoPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LineByLineDualText(
                            enText = internship.skills_required,
                            transText = getTrans(internship.skills_required_hi, internship.skills_required_ur)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (!internship.tools_technologies.isNullOrBlank() && internship.tools_technologies != "null") {
                        Text("Tools:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = IndigoPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LineByLineDualText(
                            enText = internship.tools_technologies,
                            transText = getTrans(internship.tools_technologies_hi, internship.tools_technologies_ur)
                        )
                    }
                }

                // ELIGIBILITY & SELECTION
                val eligTitle = getInlineText("Eligibility & Selection", "योग्यता और चयन", "اہلیت اور انتخاب")
                InteractiveExpandable(icon = Icons.Default.Checklist, title = eligTitle) {
                    DualLineText(enText = "Who Can Apply: ${internship.who_can_apply}", transText = getTrans("कौन आवेदन कर सकता है: ${internship.who_can_apply_hi}", "کون اپلائی کر سکتا ہے: ${internship.who_can_apply_ur}"))
                    Spacer(modifier = Modifier.height(4.dp))
                    DualLineText(enText = "Course: ${internship.course_required}", transText = getTrans("कोर्स: ${internship.course_required_hi}", "کورس: ${internship.course_required_ur}"))
                    Spacer(modifier = Modifier.height(4.dp))
                    DualLineText(enText = "Year of Study: ${internship.year_of_study}", transText = null)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Selection Process:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = IndigoPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    LineByLineDualText(
                        enText = internship.selection_process,
                        transText = getTrans(internship.selection_process_hi, internship.selection_process_ur)
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

// --- HELPER COMPOSABLES BELOW ---

@Composable
fun InfoBox(icon: ImageVector, iconBg: Color, iconColor: Color, title: String, valueEn: String?, valueTrans: String?, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Surface(modifier = Modifier.size(36.dp), shape = RoundedCornerShape(10.dp), color = iconBg) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            DualLineText(enText = valueEn, transText = valueTrans, isSmallInfo = true)
        }
    }
}

@Composable
fun DualLineText(enText: String?, transText: String?, isHeader: Boolean = false, isSmallInfo: Boolean = false) {
    Column {
        if (!enText.isNullOrBlank() && enText != "null") {
            Text(
                text = enText,
                fontSize = if(isHeader) 26.sp else if(isSmallInfo) 14.sp else 15.sp,
                fontWeight = if(isHeader) FontWeight.ExtraBold else if (isSmallInfo) FontWeight.Bold else FontWeight.SemiBold,
                color = if(isHeader) Color.White else TextDark,
                lineHeight = if(isHeader) 32.sp else 22.sp
            )
        }
        if (!transText.isNullOrBlank() && transText != "null") {
            Text(
                text = transText,
                fontSize = if(isHeader) 18.sp else if(isSmallInfo) 13.sp else 15.sp,
                color = if(isHeader) Color.White.copy(alpha=0.8f) else IndigoPrimary,
                fontWeight = if(isHeader) FontWeight.Bold else FontWeight.Medium,
                lineHeight = if(isHeader) 24.sp else 22.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun LineByLineDualText(enText: String?, transText: String?) {
    if (enText.isNullOrBlank() || enText == "null") return

    // Split text by new lines
    val enLines = enText.split("\n").filter { it.isNotBlank() }
    val transLines = transText?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()

    Column {
        enLines.forEachIndexed { index, enLine ->
            val transLine = transLines.getOrNull(index)

            // English Line
            Text(
                text = enLine.trim(),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                lineHeight = 22.sp
            )

            // Translated Line directly underneath
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(16.dp)).clickable { isExpanded = !isExpanded },
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