package com.example.taalimisafar.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taalimisafar.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // 1. Animation Logic
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = { OvershootInterpolator(1.2f).getInterpolation(it) }
            )
        )
        delay(1200)
        navController.navigate("quote_screen") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    // 2. Design
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Professional Dark Gradient Background
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF0D1117))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // --- ZOOMED IN LOGO ---
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",

            // FIX 1: 'Crop' zooms into the center of the image to fill the circle
            contentScale = ContentScale.Crop,

            modifier = Modifier
                .scale(scale.value) // Animation

                // FIX 2: Increased size from 220.dp to 280.dp (Makes it much bigger)
                .size(280.dp)

                .clip(CircleShape)  // Forces the zoomed image into a circle
        )
    }
}