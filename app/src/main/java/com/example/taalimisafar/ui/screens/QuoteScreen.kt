package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class TriLingualQuote(val en: String, val hi: String, val ur: String)

val offlineQuotes = listOf(
    TriLingualQuote(
        en = "The best way to predict the future is to create it.",
        hi = "भविष्य की भविष्यवाणी करने का सबसे अच्छा तरीका इसे बनाना है।",
        ur = "مستقبل کی پیشین گوئی کرنے کا بہترین طریقہ اسے تخلیق کرنا ہے۔"
    ),
    TriLingualQuote(
        en = "Success is not final, failure is not fatal.",
        hi = "सफलता अंतिम नहीं है, विफलता घातक नहीं है।",
        ur = "کامیابی حتمی نہیں ہے، ناکامی مہلک نہیں ہے۔"
    ),
    TriLingualQuote(
        en = "Do good and good will come to you.",
        hi = "अच्छा करो और अच्छा तुम्हारे पास आएगा।",
        ur = "اچھا کرو اور اچھا تمہارے پاس آئے گا۔"
    ),
    TriLingualQuote(
        en = "It does not matter how slowly you go as long as you do not stop",
        hi = "इससे कोई फर्क नहीं पड़ता कि आप कितनी धीरे चलते हैं, जब तक आप रुकते नहीं हैं।",
        ur = "اس سے کوئی فرق نہیں پڑتا کہ آپ کتنی آہستہ چلتے ہیں، جب تک کہ آپ رکیں نہیں۔"
    )
)

@Composable
fun QuoteScreen(onTimeout: () -> Unit) {

    // Get random quote
    val quote = remember { offlineQuotes.random() }

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "DAILY QUOTE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // English lang
            Text(
                text = quote.en,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(24.dp))

            // Hindi lang
            Text(
                text = quote.hi,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF444444) // Dark Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Urdu lang (right to left)

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = quote.ur,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF008080), // teal
                    lineHeight = 36.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}