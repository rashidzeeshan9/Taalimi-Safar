package com.example.taalimisafar.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.taalimisafar.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        profileImageUri = uri
        if (uri != null) Toast.makeText(context, "Image selected!", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFB)
        // 🔥 REMOVED THE BOTTOM BAR HERE! MainScreen handles it natively now.
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFE0F2F1)).clickable { imagePickerLauncher.launch("image/*") }, contentAlignment = Alignment.Center) {
                    if (profileImageUri != null) AsyncImage(model = profileImageUri, contentDescription = "Profile Picture", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    else Text("Profile\npic", fontSize = 12.sp, color = Color(0xFF00695C))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "@zaqwsg", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column {
                    ProfileMenuItem(title = "Points", trailingText = "0") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(title = "Progress tracker") { }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column {
                    ProfileMenuItem(title = "Edit Profile") { navController.navigate("edit_profile") }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(title = "Setting") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(title = "About Us") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(title = "Feedback") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(title = "Logout", textColor = Color.Red) {
                        viewModel.logout()
                        onNavigateToLogin()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, trailingText: String? = null, textColor: Color = Color.Black, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontSize = 16.sp, color = textColor)
        if (trailingText != null) Text(text = trailingText, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}