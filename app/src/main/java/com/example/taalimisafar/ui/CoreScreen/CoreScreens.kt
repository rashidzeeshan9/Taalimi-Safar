import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.viewmodel.CoreViewModel

// ==========================================
// ABOUT US SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(viewModel: CoreViewModel, onNavigateBack: () -> Unit) {
    val aboutData by viewModel.aboutUsData.collectAsState()

    // Automatically fetch data when the screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchAboutUs()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (aboutData == null) {
                CircularProgressIndicator() // Show loading spinner while fetching
            } else {
                Text("Welcome to Taalimi Safar", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // Displays the text from Django
                Text(aboutData!!.description, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(24.dp))

                aboutData!!.websiteLink?.let {
                    Text("Website: $it", color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                aboutData!!.contactEmail?.let {
                    Text("Contact: $it", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

// ==========================================
// FEEDBACK SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(viewModel: CoreViewModel, onNavigateBack: () -> Unit) {
    var feedbackText by remember { mutableStateOf("") }
    val feedbackStatus by viewModel.feedbackStatus.collectAsState()

    // 1. Gets the screen context
    val context = LocalContext.current

    // 2. 🔥 ADDED: Initialize the TokenManager so the button can use it
    val tokenManager = remember { com.example.taalimisafar.utils.TokenManager(context) }

    // Listens for success or failure and shows a Toast notification
    LaunchedEffect(feedbackStatus) {
        if (feedbackStatus == "Success") {
            Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
            viewModel.resetFeedbackStatus()
            onNavigateBack() // Automatically close the screen on success
        } else if (feedbackStatus != null) {
            Toast.makeText(context, feedbackStatus, Toast.LENGTH_SHORT).show()
            viewModel.resetFeedbackStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Send Feedback") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("We would love to hear from you!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                label = { Text("Your message...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (feedbackText.isNotBlank()) {
                        // 1. Grab the user's token
                        val token = tokenManager.getAccessToken()

                        if (!token.isNullOrEmpty()) {
                            // 2. Pass BOTH the token and the feedbackText
                            viewModel.submitFeedback(token, feedbackText)
                        } else {
                            Toast.makeText(context, "You must be logged in!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = feedbackText.isNotBlank()
            ) {
                Text("Submit Feedback")
            }
        }
    }
}