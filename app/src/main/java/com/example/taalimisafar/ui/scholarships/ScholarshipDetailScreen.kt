package com.example.taalimisafar.ui.scholarships

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
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
import androidx.navigation.NavController
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.ScholarshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScholarshipDetailScreen(
    navController: NavController,
    viewModel: ScholarshipViewModel
) {
    val scholarship by viewModel.selectedScholarship.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value
    val context = LocalContext.current

    scholarship?.let { item ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
                )
            },
            bottomBar = {
                if (!item.websiteLink.isNullOrBlank()) {
                    Surface(
                        color = Color.White,
                        shadowElevation = 16.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.websiteLink))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Apply on Official Website", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. HERO HEADER (Dark Blue section)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A237E))
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column {
                        val displayTitle = when (currentLanguage) {
                            AppLanguage.HINDI -> if (!item.titleHi.isNullOrBlank()) "${item.title} \n${item.titleHi}" else item.title
                            AppLanguage.URDU -> if (!item.titleUr.isNullOrBlank()) "${item.title} \n${item.titleUr}" else item.title
                            else -> item.title
                        }

                        Text(
                            text = displayTitle,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "By: ${item.providerName ?: "Government / Trust"}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color(0xFFF5F7FA)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Spacer(modifier = Modifier.height(8.dp))
                        ExpandableSection(
                            icon = Icons.Default.Description,
                            title = "Description",
                            enText = item.description,
                            translatedText = when (currentLanguage) {
                                AppLanguage.HINDI -> item.descriptionHi
                                AppLanguage.URDU -> item.descriptionUr
                                else -> null
                            },
                            isInitiallyExpanded = true
                        )

                        ExpandableSection(
                            icon = Icons.Default.Checklist,
                            title = "Eligibility Criteria",
                            enText = item.eligibility,
                            translatedText = when (currentLanguage) {
                                AppLanguage.HINDI -> item.eligibilityHi
                                AppLanguage.URDU -> item.eligibilityUr
                                else -> null
                            }
                        )

                        ExpandableSection(
                            icon = Icons.Default.Star,
                            title = "Benefits",
                            enText = item.benefits ?: "Not Specified",
                            translatedText = when (currentLanguage) {
                                AppLanguage.HINDI -> item.benefitsHi
                                AppLanguage.URDU -> item.benefitsUr
                                else -> null
                            }
                        )
                        ExpandableSection(
                            icon = Icons.Default.Folder,
                            title = "Documents Required",
                            enText = item.documentsRequired ?: "Not Specified",
                            translatedText = when (currentLanguage) {
                                AppLanguage.HINDI -> item.documentsRequiredHi
                                AppLanguage.URDU -> item.documentsRequiredUr
                                else -> null
                            }
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun ExpandableSection(
    icon: ImageVector,
    title: String,
    enText: String,
    translatedText: String?,
    isInitiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(isInitiallyExpanded) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ArrowRotation"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE8EAF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1A237E), modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 17.sp,
                    modifier = Modifier.weight(1f)
                )
                //Arrow Icon
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // English Text(Default)
                    Text(text = enText, fontSize = 15.sp, lineHeight = 24.sp, color = Color.DarkGray)

                    // Translated Text
                    if (!translatedText.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = translatedText,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF555555),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}