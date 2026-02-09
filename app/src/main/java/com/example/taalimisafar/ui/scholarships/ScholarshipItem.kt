package com.example.taalimisafar.ui.scholarships

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.Scholarship
import com.example.taalimisafar.utils.AppLanguage

@Composable
fun ScholarshipItem(
    item: Scholarship,
    currentLanguage: AppLanguage
) {
    val context = LocalContext.current
    val isActive = item.status.equals("active", ignoreCase = true)

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- 1. HEADER (Title & Status) ---
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF8E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF263238)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = item.provider, fontSize = 12.sp, color = Color.Gray)
                }

                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if(isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if(isActive) "Active" else "Expired",
                        color = if(isActive) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. INFO GRID ---
            Row(modifier = Modifier.fillMaxWidth()) {
                ScholarshipInfo(Icons.Default.Payments, "Amount / राशि", item.amount, Color(0xFF2E7D32), Modifier.weight(1f))
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.LightGray.copy(alpha = 0.4f)))
                ScholarshipInfo(Icons.Default.Event, "Last Date / अंतिम तिथि", item.lastDate, Color(0xFFD32F2F), Modifier.weight(1f).padding(start = 16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. DESCRIPTION (Line-by-Line) ---
            if (item.description.isNotEmpty()) {
                Text("About", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                if (currentLanguage == AppLanguage.NONE) {
                    Text(item.description, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF455A64))
                } else {
                    val translation = if (currentLanguage == AppLanguage.HINDI) item.descriptionHi else item.descriptionUr
                    InterleavedText(english = item.description, translated = translation, language = currentLanguage)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // --- 4. ELIGIBILITY (Line-by-Line Fixed) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Eligibility Criteria", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentLanguage == AppLanguage.NONE) {
                        Text(item.eligibility, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF37474F))
                    } else {
                        val translation = if (currentLanguage == AppLanguage.HINDI) item.eligibilityHi else item.eligibilityUr
                        // ✅ NOW USING INTERLEAVED TEXT HERE TOO
                        InterleavedText(english = item.eligibility, translated = translation, language = currentLanguage)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 5. APPLY BUTTON ---
            Button(
                onClick = {
                    item.websiteLink?.let { url ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                },
                enabled = isActive && !item.websiteLink.isNullOrBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if(isActive) Color(0xFF1A237E) else Color.Gray),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if(currentLanguage == AppLanguage.HINDI) "Apply Now / आवेदन करें" else "Apply Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun ScholarshipInfo(icon: ImageVector, label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 11.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
}

// --- ✅ UPDATED: Handles Newlines AND Periods ---
@Composable
fun InterleavedText(english: String, translated: String?, language: AppLanguage) {
    if (translated.isNullOrBlank()) {
        Text(text = english, color = Color(0xFF455A64), style = MaterialTheme.typography.bodyMedium)
        return
    }

    // 1. Split English by Newline OR Period
    // This fixes bullet points or lists in eligibility
    val engSentences = english.split("\n", ".").map { it.trim() }.filter { it.isNotEmpty() }

    // 2. Split Translation
    val transDelimiters = if (language == AppLanguage.HINDI) charArrayOf('|', '.', '।', '\n') else charArrayOf('۔', '.', '\n')
    val transSentences = translated.split(*transDelimiters).map { it.trim() }.filter { it.isNotEmpty() }

    val maxLines = maxOf(engSentences.size, transSentences.size)
    val isUrdu = (language == AppLanguage.URDU)

    Column {
        for (i in 0 until maxLines) {
            // English Line
            if (i < engSentences.size) {
                Text(
                    text = "• " + engSentences[i], // Added bullet for clarity
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            // Translated Line
            if (i < transSentences.size) {
                Text(
                    text = transSentences[i],
                    color = if (isUrdu) Color(0xFF00695C) else Color(0xFF1565C0),
                    textAlign = if (isUrdu) TextAlign.End else TextAlign.Start,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 8.dp)
                )
            }
        }
    }
}