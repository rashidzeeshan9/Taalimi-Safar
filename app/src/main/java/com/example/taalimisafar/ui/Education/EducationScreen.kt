package com.example.taalimisafar.ui.education

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// 🔥 Changed 'route: String' to 'id: Int' so we can fetch data from Django
data class EducationItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(navController: NavController) {

    // ⚠️ IMPORTANT: Change these 'id' numbers (1, 2, 3...) to match the exact
    // EducationBoard IDs you have in your Django Admin Panel!
    val educationBoards = listOf(
        EducationItem(id = 1, title = "CBSE", subtitle = "All India", icon = Icons.Default.School, color = Color(0xFF1E88E5)),
        EducationItem(id = 2, title = "NIOS", subtitle = "All India", icon = Icons.Default.MenuBook, color = Color(0xFF43A047)),
        EducationItem(id = 3, title = "PSEB", subtitle = "Punjab", icon = Icons.Default.AccountBalance, color = Color(0xFFF4511E)),
        EducationItem(id = 4, title = "UPMSP", subtitle = "Uttar Pradesh", icon = Icons.Default.LocationOn, color = Color(0xFF6D4C41)),
        EducationItem(id = 5, title = "MSBSHSE", subtitle = "Maharashtra", icon = Icons.Default.Groups, color = Color(0xFF8E24AA)),
        EducationItem(id = 6, title = "BSEH", subtitle = "Haryana", icon = Icons.Default.Assignment, color = Color(0xFF00897B)),
        EducationItem(id = 7, title = "Jamia", subtitle = "New Delhi", icon = Icons.Default.AccountBalance, color = Color(0xFFD81B60)),
        EducationItem(id = 8, title = "BSEB", subtitle = "Bihar", icon = Icons.Default.Description, color = Color(0xFF3949AB))
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Education / School",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(educationBoards) { item ->
                EducationCard(item = item, navController = navController)
            }
        }
    }
}

@Composable
fun EducationCard(
    item: EducationItem,
    navController: NavController
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable {
                // 🔥 Passes the backend ID directly to the detail screen!
                navController.navigate("board_detail/${item.id}")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center
            )

            Text(
                text = item.subtitle,
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}