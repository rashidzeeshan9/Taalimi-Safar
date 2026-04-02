package com.example.taalimisafar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.data.model.CommunityQuestion
import com.example.taalimisafar.viewmodel.CommunityViewModel
@Composable
private fun CommunityDualLabel(
    en: String,
    hi: String,
    ur: String,
    enFontSize: androidx.compose.ui.unit.TextUnit = 18.sp,
    transFontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    enColor: Color,
    transColor: Color,
    enFontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier
) {
    val selectedLanguage = com.example.taalimisafar.utils.LanguageManager.currentLanguage.value
    val secondaryText = when (selectedLanguage) {
        com.example.taalimisafar.utils.AppLanguage.HINDI -> hi.takeIf { it.isNotBlank() }
        com.example.taalimisafar.utils.AppLanguage.URDU -> ur.takeIf { it.isNotBlank() }
        else -> null
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text(
            text = en,
            fontSize = enFontSize,
            fontWeight = enFontWeight,
            color = enColor
        )
        if (!secondaryText.isNullOrBlank() && secondaryText != en) {
            Text(
                text = secondaryText,
                fontSize = transFontSize,
                fontWeight = FontWeight.SemiBold,
                color = transColor,
                lineHeight = transFontSize * 1.25f,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun CommunityDualSupportingText(
    hi: String,
    ur: String,
    color: Color = Color(0xFF4F46E5)
) {
    val selectedLanguage = com.example.taalimisafar.utils.LanguageManager.currentLanguage.value
    val t = when (selectedLanguage) {
        com.example.taalimisafar.utils.AppLanguage.HINDI -> hi.takeIf { it.isNotBlank() }
        com.example.taalimisafar.utils.AppLanguage.URDU -> ur.takeIf { it.isNotBlank() }
        else -> null
    }
    if (t.isNullOrBlank()) return
    Text(
        text = t,
        fontSize = 11.sp,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

private data class CommunityBilingualOption(
    val en: String,
    val hi: String,
    val ur: String
)

private val communityLanguageOptions = listOf(
    CommunityBilingualOption("English", "अंग्रेज़ी", "انگریزی"),
    CommunityBilingualOption("Roman English", "रोमन अंग्रेज़ी", "رومن انگریزی")
)

private val communityCategoryOptions = listOf(
    CommunityBilingualOption("Education", "शिक्षा", "تعلیم"),
    CommunityBilingualOption("Skills", "कौशल", "مہارت"),
    CommunityBilingualOption("Career", "करियर", "کیریئر"),
    CommunityBilingualOption("Govt. Job", "सरकारी नौकरी", "سرکاری نوکری"),
    CommunityBilingualOption("Exam Date", "परीक्षा तिथि", "امتحان کی تاریخ"),
    CommunityBilingualOption("Business", "व्यवसाय", "کاروبار")
)

private fun categoryBilingualFor(en: String): CommunityBilingualOption? =
    communityCategoryOptions.find { it.en == en }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
    navController: NavController,
    viewModel: CommunityViewModel = viewModel()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // Form States
    var expandedLanguage by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("") }

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }

    var questionText by remember { mutableStateOf("") }

    // Questions State
    val questions by viewModel.questions.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A237E), Color(0xFF303F9F))
                        )
                    )
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Top Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            CommunityDualLabel(
                                en = "Society for Education & Skills",
                                hi = "शिक्षा एवं कौशल हेतु समाज",
                                ur = "تعلیم اور مہارت کے لیے سوسائٹی",
                                enFontSize = 22.sp,
                                transFontSize = 13.sp,
                                enColor = Color.White,
                                transColor = Color.White.copy(alpha = 0.88f)
                            )
                            CommunityDualLabel(
                                en = "Ask, Answer, and Connect",
                                hi = "पूछें, उत्तर दें और जुड़ें",
                                ur = "پوچھیں، جواب دیں اور جڑیں",
                                enFontSize = 14.sp,
                                transFontSize = 12.sp,
                                enColor = Color.White.copy(alpha = 0.92f),
                                transColor = Color.White.copy(alpha = 0.78f),
                                enFontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Theme Toggle",
                                tint = Color.White
                            )
                        }
                    }

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            CommunityDualLabel(
                                en = "Search society...",
                                hi = "समाज में खोजें...",
                                ur = "سوسائٹی میں تلاش کریں...",
                                enFontSize = 16.sp,
                                transFontSize = 12.sp,
                                enColor = Color.Gray,
                                transColor = Color.Gray.copy(alpha = 0.85f),
                                enFontWeight = FontWeight.Normal
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF1A237E))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color(0xFF1A237E)
                        ),
                        singleLine = true
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. The Submit Form Card
            item {
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CommunityDualLabel(
                            en = "Ask a Question",
                            hi = "प्रश्न पूछें",
                            ur = "سوال پوچھیں",
                            enFontSize = 18.sp,
                            transFontSize = 14.sp,
                            enColor = Color(0xFF1A237E),
                            transColor = Color(0xFF4F46E5),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Language Selection
                            ExposedDropdownMenuBox(
                                expanded = expandedLanguage,
                                onExpandedChange = { expandedLanguage = !expandedLanguage },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedLanguage,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = {
                                        CommunityDualLabel(
                                            en = "Language",
                                            hi = "भाषा",
                                            ur = "زبان",
                                            enFontSize = 14.sp,
                                            transFontSize = 11.sp,
                                            enColor = Color(0xFF1A237E),
                                            transColor = Color(0xFF4F46E5),
                                            enFontWeight = FontWeight.Medium
                                        )
                                    },
                                    supportingText = {
                                        communityLanguageOptions.find { it.en == selectedLanguage }?.let { opt ->
                                            CommunityDualSupportingText(opt.hi, opt.ur)
                                        }
                                    },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLanguage) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedLanguage,
                                    onDismissRequest = { expandedLanguage = false }
                                ) {
                                    communityLanguageOptions.forEach { opt ->
                                        DropdownMenuItem(
                                            text = {
                                                CommunityDualLabel(
                                                    en = opt.en,
                                                    hi = opt.hi,
                                                    ur = opt.ur,
                                                    enFontSize = 15.sp,
                                                    transFontSize = 12.sp,
                                                    enColor = Color.Black,
                                                    transColor = Color(0xFF4F46E5),
                                                    enFontWeight = FontWeight.Medium
                                                )
                                            },
                                            onClick = {
                                                selectedLanguage = opt.en
                                                expandedLanguage = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Category Selection
                            ExposedDropdownMenuBox(
                                expanded = expandedCategory,
                                onExpandedChange = { expandedCategory = !expandedCategory },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = {
                                        CommunityDualLabel(
                                            en = "Category",
                                            hi = "श्रेणी",
                                            ur = "زمرہ",
                                            enFontSize = 14.sp,
                                            transFontSize = 11.sp,
                                            enColor = Color(0xFF1A237E),
                                            transColor = Color(0xFF4F46E5),
                                            enFontWeight = FontWeight.Medium
                                        )
                                    },
                                    supportingText = {
                                        communityCategoryOptions.find { it.en == selectedCategory }?.let { opt ->
                                            CommunityDualSupportingText(opt.hi, opt.ur)
                                        }
                                    },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCategory,
                                    onDismissRequest = { expandedCategory = false }
                                ) {
                                    communityCategoryOptions.forEach { opt ->
                                        DropdownMenuItem(
                                            text = {
                                                CommunityDualLabel(
                                                    en = opt.en,
                                                    hi = opt.hi,
                                                    ur = opt.ur,
                                                    enFontSize = 15.sp,
                                                    transFontSize = 12.sp,
                                                    enColor = Color.Black,
                                                    transColor = Color(0xFF4F46E5),
                                                    enFontWeight = FontWeight.Medium
                                                )
                                            },
                                            onClick = {
                                                selectedCategory = opt.en
                                                expandedCategory = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CommunityDualLabel(
                            en = "Question",
                            hi = "प्रश्न",
                            ur = "سوال",
                            enFontSize = MaterialTheme.typography.titleMedium.fontSize,
                            transFontSize = 14.sp,
                            enColor = Color.Black,
                            transColor = Color(0xFF4F46E5),
                            enFontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = questionText,
                            onValueChange = { questionText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            placeholder = {
                                CommunityDualLabel(
                                    en = "Write your question here...",
                                    hi = "अपना प्रश्न यहाँ लिखें...",
                                    ur = "اپنا سوال یہاں لکھیں...",
                                    enFontSize = 15.sp,
                                    transFontSize = 12.sp,
                                    enColor = Color.Gray,
                                    transColor = Color.Gray.copy(alpha = 0.85f),
                                    enFontWeight = FontWeight.Normal
                                )
                            },
                            singleLine = false,
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1A237E)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (selectedLanguage.isEmpty() || selectedCategory.isEmpty() || questionText.isBlank()) {
                                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    Toast.makeText(context, "Submitted to App Admin", Toast.LENGTH_LONG).show()
                                    questionText = ""
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                            ) {
                                CommunityDualLabel(
                                    en = "To Admin",
                                    hi = "व्यवस्थापक को",
                                    ur = "منتظم کو",
                                    enFontSize = 12.sp,
                                    transFontSize = 10.sp,
                                    enColor = Color.White,
                                    transColor = Color.White.copy(alpha = 0.92f),
                                    enFontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            Button(
                                onClick = {
                                    if (selectedLanguage.isEmpty() || selectedCategory.isEmpty() || questionText.isBlank()) {
                                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    viewModel.addQuestion("You", selectedCategory, questionText)
                                    Toast.makeText(context, "Submitted to Everyone", Toast.LENGTH_LONG).show()
                                    questionText = ""
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
                            ) {
                                CommunityDualLabel(
                                    en = "To Everyone",
                                    hi = "सभी को",
                                    ur = "سب کو",
                                    enFontSize = 12.sp,
                                    transFontSize = 10.sp,
                                    enColor = Color.White,
                                    transColor = Color.White.copy(alpha = 0.92f),
                                    enFontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // 2. Feed Header
            item {
                CommunityDualLabel(
                    en = "Recent Questions",
                    hi = "हाल के प्रश्न",
                    ur = "حالیہ سوالات",
                    enFontSize = 18.sp,
                    transFontSize = 14.sp,
                    enColor = Color.Black,
                    transColor = Color(0xFF4F46E5),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 3. List of Questions
            items(questions) { q ->
                QuestionCard(
                    question = q,
                    onQuestionClick = { navController.navigate("communityDetail/${q.id}") },
                    onUpvote = { viewModel.toggleUpvote(q.id) },
                    onDownvote = { viewModel.toggleDownvote(q.id) }
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: CommunityQuestion,
    onQuestionClick: () -> Unit,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onQuestionClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Author & Follow button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = question.author, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        val cat = categoryBilingualFor(question.category)
                        CommunityDualLabel(
                            en = "Category: ${question.category}",
                            hi = "श्रेणी: ${cat?.hi ?: question.category}",
                            ur = "زمرہ: ${cat?.ur ?: question.category}",
                            enFontSize = 12.sp,
                            transFontSize = 10.sp,
                            enColor = Color.Gray,
                            transColor = Color(0xFF5C6BC0),
                            enFontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body
            Text(
                text = question.text,
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Upvote / Downvote / Answers
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onUpvote,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Upvote",
                            tint = if (question.userVote == 1) Color(0xFF1A237E) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(text = "${question.upvotes}", fontSize = 14.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = onDownvote,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Downvote",
                            tint = if (question.userVote == -1) Color(0xFFF44336) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(text = "${question.downvotes}", fontSize = 14.sp, color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Answers", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    CommunityDualLabel(
                        en = "Answers",
                        hi = "उत्तर",
                        ur = "جوابات",
                        enFontSize = 14.sp,
                        transFontSize = 11.sp,
                        enColor = Color.Gray,
                        transColor = Color.Gray.copy(alpha = 0.88f),
                        enFontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
