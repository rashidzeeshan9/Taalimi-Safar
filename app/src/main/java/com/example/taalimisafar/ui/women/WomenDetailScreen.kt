package com.example.taalimisafar.ui.women

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.WomenViewModel

// Theme Colors
private val PinkPrimary = Color(0xFFE11D48)
private val PinkLight = Color(0xFFFFE4E6)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WomenDetailScreen(
    programId: Int,
    viewModel: WomenViewModel,
    onBackClick: () -> Unit
) {
    val language = LanguageManager.currentLanguage.value
    val program by viewModel.selectedProgram.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(programId) {
        viewModel.fetchProgramDetail(programId)
    }

    if (program == null) return

    fun getTrans(hi: String?, ur: String?): String? = when (language) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkPrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(padding).verticalScroll(rememberScrollState())) {

            // --- HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PinkPrimary, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .padding(top = 8.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                DualLineText(
                    enText = program!!.title,
                    transText = getTrans(program!!.title_hi, program!!.title_ur),
                    isHeader = true
                )
            }

            // --- MAIN CONTENT ---
            Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-30).dp)) {

                // OVERVIEW
                InteractiveExpandable(icon = Icons.Default.Info, title = "Overview", isInitiallyExpanded = true) {
                    LineByLineDualText(
                        enText = program!!.description,
                        transText = getTrans(program!!.description_hi, program!!.description_ur)
                    )
                }

                // SUPPORT CONTACTS (Emergency logic)
                if (!program!!.support_contacts.isNullOrEmpty()) {
                    InteractiveExpandable(icon = Icons.Default.Call, title = "Support Contacts", isInitiallyExpanded = true) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            program!!.support_contacts!!.forEach { contact ->
                                val isEmergency = contact.is_emergency_helpline
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if(isEmergency) Color(0xFFFEF2F2) else Color(0xFFF8FAFC)),
                                    border = if(isEmergency) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)) else null
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if(isEmergency) Icons.Default.Warning else Icons.Default.Phone, null, tint = if(isEmergency) Color(0xFFDC2626) else PinkPrimary, modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            DualLineText(enText = contact.name, transText = getTrans(contact.name_hi, contact.name_ur), isDescription = false)
                                            Text(contact.phone, fontWeight = FontWeight.Bold, color = if(isEmergency) Color(0xFFDC2626) else TextDark, fontSize = 16.sp, modifier = Modifier.padding(top=4.dp))
                                        }
                                        IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))) }) {
                                            Icon(Icons.Default.PhoneForwarded, "Call", tint = if(isEmergency) Color(0xFFDC2626) else PinkPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // GOVERNMENT SCHEMES
                if (!program!!.schemes.isNullOrEmpty()) {
                    InteractiveExpandable(icon = Icons.Default.AccountBalance, title = "Government Schemes") {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            program!!.schemes!!.forEach { scheme ->
                                Column {
                                    DualLineText(enText = scheme.title, transText = getTrans(scheme.title_hi, scheme.title_ur), isDescription = false)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LineByLineDualText(enText = scheme.description, transText = getTrans(scheme.description_hi, scheme.description_ur))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun DualLineText(enText: String?, transText: String?, isHeader: Boolean = false, isDescription: Boolean = false) {
    Column {
        if (!enText.isNullOrBlank() && enText != "null") {
            Text(
                text = enText,
                fontSize = if(isHeader) 26.sp else if(isDescription) 14.sp else 16.sp,
                fontWeight = if(isHeader) FontWeight.ExtraBold else if (isDescription) FontWeight.Medium else FontWeight.Bold,
                color = if(isHeader) Color.White else if(isDescription) TextMuted else TextDark,
                lineHeight = if(isHeader) 32.sp else 22.sp
            )
        }
        if (!transText.isNullOrBlank() && transText != "null") {
            Text(
                text = transText,
                fontSize = if(isHeader) 18.sp else if(isDescription) 13.sp else 14.sp,
                color = if(isHeader) Color.White.copy(alpha=0.8f) else PinkPrimary,
                fontWeight = if(isHeader) FontWeight.Bold else FontWeight.Medium,
                lineHeight = if(isHeader) 24.sp else 20.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun LineByLineDualText(enText: String?, transText: String?) {
    if (enText.isNullOrBlank() || enText == "null") return

    val enLines = enText.split("\n").filter { it.isNotBlank() }
    val transLines = transText?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()

    Column {
        enLines.forEachIndexed { index, enLine ->
            val transLine = transLines.getOrNull(index)

            Text(
                text = enLine.trim(),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                lineHeight = 22.sp
            )

            if (!transLine.isNullOrBlank() && transLine != "null") {
                Text(
                    text = transLine.trim(),
                    fontSize = 14.sp,
                    color = PinkPrimary,
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
    val bgColor by animateColorAsState(targetValue = if (isExpanded) Color(0xFFFFF1F2) else Color.White, label = "BgColor")

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(RoundedCornerShape(16.dp)).clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(PinkLight, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = PinkPrimary, modifier = Modifier.size(20.dp))
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