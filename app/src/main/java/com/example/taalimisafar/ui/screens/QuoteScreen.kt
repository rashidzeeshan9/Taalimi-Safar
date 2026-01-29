package com.example.taalimisafar.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.QuoteViewModel

// 1. Main Screen Wrapper
@Composable
fun QuoteScreen(
    navController: NavController,
    viewModel: QuoteViewModel = viewModel()
) {
    val quoteData by viewModel.quoteState.collectAsState()

    if (quoteData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF1A237E))
        }
    } else {
        QuoteScreenContent(
            quote = quoteData!!,
            onLanguageSelected = { selectedLanguage ->
                LanguageManager.currentLanguage.value = selectedLanguage
                navController.navigate("home_screen") {
                    popUpTo("quote_screen") { inclusive = true }
                }
            }
        )
    }
}

// 2. The Interactive UI Content
@Composable
fun QuoteScreenContent(
    quote: Quote,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // DESIGN UPGRADE: Subtle Gradient Background
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFDFBF7), Color(0xFFF3E5F5)) // Cream to Light Lavender
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- DECORATIVE QUOTE CARD ---
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "“${quote.text}”",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF263238),
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "- ${quote.author}",
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- LANGUAGE SELECTION ---
            Text(
                text = "Choose your Secondary language",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF546E7A),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // HINDI CARD (Orange + Book Icon)
                InteractiveLanguageCard(
                    englishName = "Hindi",
                    nativeName = "हिंदी",
                    icon = Icons.Default.MenuBook,
                    color1 = Color(0xFFEF6C00),
                    color2 = Color(0xFFFF9800),
                    onClick = { onLanguageSelected(AppLanguage.HINDI) }
                )

                InteractiveLanguageCard(
                    englishName = "Urdu",
                    nativeName = "اردو",
                    icon = Icons.Default.Edit,
                    color1 = Color(0xFF00695C),
                    color2 = Color(0xFF26A69A),
                    onClick = { onLanguageSelected(AppLanguage.URDU) }
                )
            }
        }
    }
}

// 3. Interactive Card Component with Animation
@Composable
fun InteractiveLanguageCard(
    englishName: String,
    nativeName: String,
    icon: ImageVector,
    color1: Color,
    color2: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animation: Shrink slightly when pressed
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .size(140.dp)
            .scale(scale) // Apply animation
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(color1, color2) // Beautiful Gradient
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Decorative faded circle in background
            Box(
                modifier = Modifier
                    .offset(x = 40.dp, y = (-40).dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Native Text
                Text(
                    text = nativeName,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // English Text
                Text(
                    text = englishName,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}