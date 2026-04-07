package com.example.taalimisafar.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

    // Automatically fetch the profile if the user is logged in but data is missing
    LaunchedEffect(Unit) {
        if (viewModel.getAccessToken() != null && viewModel.userProfile.value == null) {
            viewModel.fetchProfile()
        }
    }

    val profile = viewModel.userProfile.value
    val displayUsername = profile?.custom_username ?: "Loading..."
    val displayPoints = profile?.total_points?.toString() ?: "0"

    var localImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        localImageUri = uri
        if (uri != null) Toast.makeText(context, "Image selected!", Toast.LENGTH_SHORT).show()
    }

    val imageToDisplay = localImageUri ?: profile?.profile_pic

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // HEADER SECTION
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageToDisplay != null) {
                        AsyncImage(
                            model = imageToDisplay,
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Pic",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = displayUsername,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    // Removed the "Student" qualification text from here!
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // STATUS DASHBOARD (Points, Progress, Quiz)
            // ==========================================
            ProfileStatusCard(
                points = displayPoints,
                onProgressClick = { Toast.makeText(context, "Progress tracker coming soon!", Toast.LENGTH_SHORT).show() },
                onDailyQuizClick = { Toast.makeText(context, "Daily Quiz coming soon!", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // ACTIONS & SETTINGS
            // ==========================================
            Text(
                text = "Account & Settings",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ProfileActionItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        iconTint = MaterialTheme.colorScheme.primary
                    ) { navController.navigate("edit_profile") }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    ProfileActionItem(
                        icon = Icons.Default.Info,
                        title = "About Us",
                        iconTint = MaterialTheme.colorScheme.primary
                    ) { }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    ProfileActionItem(
                        icon = Icons.Default.Feedback,
                        title = "Feedback",
                        iconTint = MaterialTheme.colorScheme.primary
                    ) { }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    ProfileActionItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Logout",
                        textColor = MaterialTheme.colorScheme.error,
                        iconTint = MaterialTheme.colorScheme.error,
                        showArrow = false
                    ) {
                        viewModel.logout()
                        onNavigateToLogin()
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ==========================================
// CUSTOM COMPONENT: STATUS CARD (COMBINED)
// ==========================================
@Composable
fun ProfileStatusCard(
    points: String,
    onProgressClick: () -> Unit,
    onDailyQuizClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "My Activity",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Points Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = null,
                    tint = Color(0xFFFFC107), // Gold
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "$points Points",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Total rewards earned",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(20.dp))

            // 2. Daily Quiz Row (Moved up here!)
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onDailyQuizClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFFF9800), // Vibrant Orange
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Play Daily Quiz",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Progress Tracker Row
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onProgressClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "View progress tracker",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==========================================
// CUSTOM COMPONENT: ACTION LIST ITEM
// ==========================================
@Composable
fun ProfileActionItem(
    icon: ImageVector,
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}