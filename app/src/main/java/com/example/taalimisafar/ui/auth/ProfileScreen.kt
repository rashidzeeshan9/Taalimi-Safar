package com.example.taalimisafar.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// 🔥 THIS IS THE FIX FOR THE COLOR ERRORS 🔥
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit // Passed from your navigation graph to handle the logout click
) {
    // Fetch profile data when the screen opens
    LaunchedEffect(Unit) { viewModel.fetchProfile() }
    val profile = viewModel.userProfile.value

    if (profile == null) {
        // --- LOADING STATE ---
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Loading secure profile data...")
        }
    } else {
        // --- ACTUAL PROFILE UI ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- HEADER SECTION ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color(0xFFD1E8E2)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Profile\npic", textAlign = TextAlign.Center, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "@${profile.custom_username}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- STATS GROUP ---
            ProfileCard {
                ProfileMenuItem("Points", trailingText = "${profile.total_points}")
                Divider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                ProfileMenuItem("Progress tracker")
            }

            Spacer(Modifier.height(16.dp))

            // --- OPTIONS GROUP ---
            ProfileCard {
                ProfileMenuItem("Edit Profile")
                Divider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                ProfileMenuItem("Setting")
                Divider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                ProfileMenuItem("About Us")
                Divider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                ProfileMenuItem("Feedback")
                Divider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                // Logout Button
                ProfileMenuItem("Logout", textColor = Color.Red, onClick = {
                    viewModel.logout()
                    onNavigateToLogin()
                })
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        content = content
    )
}

@Composable
fun ProfileMenuItem(
    title: String,
    trailingText: String? = null,
    textColor: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = textColor, fontSize = 16.sp)
        if (trailingText != null) {
            Text(trailingText, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}