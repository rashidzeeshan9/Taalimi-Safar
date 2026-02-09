package com.example.taalimisafar.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// ---------- DATA MODEL ----------
data class HomeGridItem(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color,
    val route: String
)

// ---------- HOME SCREEN ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val items = listOf(
        HomeGridItem("Academic", Icons.Default.School, Color(0xFFFFC107), "academic"),
        HomeGridItem("Diploma", Icons.Default.MenuBook, Color(0xFFFFFF00), "diploma"),
        HomeGridItem("Women’s Empowerment", Icons.Default.Female, Color(0xFFE3F2FD), "women"),
        HomeGridItem("Internships", Icons.Default.Work, Color(0xFFA5D6A7), "internships"),
        HomeGridItem("Skill Development", Icons.Default.Build, Color(0xFFFFCC80), "skills"),
        HomeGridItem("Important Dates", Icons.Default.Event, Color(0xFFFFFF99), "important_dates"),
        HomeGridItem("Govt Jobs", Icons.Default.AccountBalance, Color(0xFFB0BEC5), "govt_jobs"),
        HomeGridItem("Private Jobs", Icons.Default.BusinessCenter, Color(0xFFFFFF00), "private_jobs"),
        HomeGridItem("Govt Schemes", Icons.Default.Assignment, Color(0xFFAED581), "govt_schemes"),
        HomeGridItem("Career in Sports", Icons.Default.SportsSoccer, Color(0xFFFFE0B2), "sports"),
        HomeGridItem("Religious Studies", Icons.Default.MenuBook, Color(0xFFE0E0E0), "religion"),
        HomeGridItem("Good Hobbies", Icons.Default.Star, Color(0xFFFFCCBC), "hobbies")
    )

    val filteredItems = items.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Taalimi Safar", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.DarkMode,
                            contentDescription = "Theme Toggle"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 🔍 SEARCH BAR (BELOW HEADING)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search categories…") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { searchQuery = "" }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 EMPTY STATE ANIMATION
            AnimatedVisibility(
                visible = filteredItems.isEmpty(),
                enter = fadeIn(tween(300)) + scaleIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No results found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 🔢 2 × 6 GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems, key = { it.title }) { item ->
                    HomeGridCard(item) {
                        navController.navigate(item.route)
                    }
                }
            }
        }
    }
}

// ---------- GRID CARD ----------
@Composable
fun HomeGridCard(
    item: HomeGridItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1.2f)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = item.bgColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(item.icon, contentDescription = item.title, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(10.dp))
            Text(
                item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }
    }
}
