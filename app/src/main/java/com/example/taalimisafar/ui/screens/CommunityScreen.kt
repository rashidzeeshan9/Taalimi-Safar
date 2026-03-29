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
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager

/** English on first line; Hindi or Urdu on second line when that language is opted (like Diploma). */
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
    val lang = LanguageManager.currentLanguage.value
    val secondaryLine = when (lang) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        AppLanguage.NONE -> {
            val parts = listOfNotNull(hi.takeIf { it.isNotBlank() }, ur.takeIf { it.isNotBlank() })
            if (parts.isEmpty()) null else parts.joinToString(" · ")
        }
    }
    Column(modifier = modifier) {
        Text(
            text = en,
            fontSize = enFontSize,
            fontWeight = enFontWeight,
            color = enColor
        )
        if (!secondaryLine.isNullOrBlank() && secondaryLine != en) {
            Text(
                text = secondaryLine,
                fontSize = transFontSize,
                fontWeight = FontWeight.SemiBold,
                color = transColor,
                lineHeight = transFontSize * 1.25f,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// Mock Data
data class CommunityQuestion(
    val id: String,
    val author: String,
    val category: String,
    val text: String,
    var upvotes: Int,
    var downvotes: Int,
    var isFollowing: Boolean = false,
    var userVote: Int = 0 // 1 for up, -1 for down, 0 for none
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // Form States
    var expandedLanguage by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("") }
    val languages = listOf("English", "Roman English")

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    val categories = listOf("Education", "Skills", "Career", "Govt. Job", "Exam Date", "Business")

    var questionText by remember { mutableStateOf("") }

    // Mock Questions State
    val questions = remember {
        mutableStateListOf(
            CommunityQuestion("1", "Ali Khan", "Education", "What are the best scholarships for studying abroad in 2024?", 15, 2),
            CommunityQuestion("2", "Aisha", "Govt. Job", "Is there any upcoming vacancy for bank PO exam?", 42, 1),
            CommunityQuestion("3", "Rahul", "Skills", "How to improve English communication skills?", 10, 0)
        )
    }

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
                            Text(
                                text = "Ask, Answer, and Connect",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f),
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
                            Text("Search community...", color = Color.Gray)
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
                                    label = { Text("Language") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLanguage) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedLanguage,
                                    onDismissRequest = { expandedLanguage = false }
                                ) {
                                    languages.forEach { language ->
                                        DropdownMenuItem(
                                            text = { Text(language) },
                                            onClick = {
                                                selectedLanguage = language
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
                                    label = { Text("Category") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCategory,
                                    onDismissRequest = { expandedCategory = false }
                                ) {
                                    categories.forEach { category ->
                                        DropdownMenuItem(
                                            text = { Text(category) },
                                            onClick = {
                                                selectedCategory = category
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
                            placeholder = { Text("Write your question here...") },
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
                                    Toast.makeText(context, "Submitted to Everyone (Pending)", Toast.LENGTH_LONG).show()
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
                QuestionCard(question = q)
            }
        }
    }
}

@Composable
fun QuestionCard(question: CommunityQuestion) {
    var upvotes by remember { mutableIntStateOf(question.upvotes) }
    var downvotes by remember { mutableIntStateOf(question.downvotes) }
    var userVote by remember { mutableIntStateOf(question.userVote) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
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
                        Text(text = "Category: ${question.category}", fontSize = 12.sp, color = Color.Gray)
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
                        onClick = {
                            if (userVote == 1) {
                                userVote = 0
                                upvotes--
                            } else {
                                if (userVote == -1) downvotes--
                                userVote = 1
                                upvotes++
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Upvote",
                            tint = if (userVote == 1) Color(0xFF1A237E) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(text = "$upvotes", fontSize = 14.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            if (userVote == -1) {
                                userVote = 0
                                downvotes--
                            } else {
                                if (userVote == 1) upvotes--
                                userVote = -1
                                downvotes++
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Downvote",
                            tint = if (userVote == -1) Color(0xFFF44336) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(text = "$downvotes", fontSize = 14.sp, color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Answers", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Answers", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
