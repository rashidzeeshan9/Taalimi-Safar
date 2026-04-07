package com.example.taalimisafar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.taalimisafar.data.model.CommunityQuestion
import com.example.taalimisafar.viewmodel.AuthViewModel
import com.example.taalimisafar.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen(
    questionId: Int, // ✅ Must be Int
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: CommunityViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val questions by viewModel.questions.collectAsState()
    val question = questions.find { it.id == questionId }

    var answerText by remember { mutableStateOf("") }
    val userToken = authViewModel.getAccessToken() ?: ""
    val isAuthenticated by authViewModel.isAuthenticated

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Question Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        if (question == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Question not found.")
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { QuestionDetailsCard(question) }

                    item {
                        val answerCount = question.answers?.size ?: 0
                        Text(
                            text = "Answers ($answerCount)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFF1A237E)
                        )
                    }

                    items(question.answers ?: emptyList()) { answer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    ProfilePicture(url = answer.authorProfilePic)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = answer.author ?: "Anonymous", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                    if (answer.isAdmin) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                                            Text(
                                                text = "Admin",
                                                color = Color(0xFF2E7D32),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = answer.text ?: "", fontSize = 14.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }

                if (isAuthenticated) {
                    Surface(color = Color.White, shadowElevation = 8.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = answerText,
                                onValueChange = { answerText = it },
                                placeholder = { Text("Write your response...") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1A237E))
                            )
                            IconButton(
                                onClick = {
                                    if (answerText.isNotBlank() && userToken.isNotEmpty()) {
                                        viewModel.postAnswer(token = userToken, questionId = questionId, text = answerText)
                                        answerText = ""
                                        Toast.makeText(context, "Answer posted!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.clip(CircleShape).background(Color(0xFF1A237E))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Post",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                } else {
                    Surface(color = Color.White, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Log in to join this discussion", color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onNavigateToLogin,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text("Log in")
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SHARED COMPONENTS ---
@Composable
fun ProfilePicture(url: String?, modifier: Modifier = Modifier.size(36.dp)) {
    if (!url.isNullOrBlank()) {
        AsyncImage(
            model = url,
            contentDescription = "Profile Pic",
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(modifier = modifier.clip(CircleShape).background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun QuestionDetailsCard(question: CommunityQuestion) {
    // 🔥 THE FIX: Safely convert category numbers to names for the details screen
    val resolvedCategory = when (question.category) {
        "1" -> "Education"
        "2" -> "Skills"
        "3" -> "Career"
        "4" -> "Govt. Job"
        "5" -> "Exam Date"
        "6" -> "Business"
        else -> question.category ?: "General"
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfilePicture(url = question.authorProfilePic)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = question.author ?: "Anonymous", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Row {
                        // 🔥 Ensure resolvedCategory is displayed here
                        Text(text = "$resolvedCategory • ", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = "To: ${question.target ?: "Everyone"}",
                            fontSize = 12.sp,
                            color = if (question.target == "Admin") Color(0xFF1A237E) else Color.Gray,
                            fontWeight = if (question.target == "Admin") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = question.text ?: "", fontSize = 16.sp, color = Color.Black, lineHeight = 22.sp)
        }
    }
}