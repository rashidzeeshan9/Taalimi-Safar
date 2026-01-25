package com.example.taalimisafar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.utils.AppLanguage

@Composable
fun QuoteScreen(
    quote: Quote,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    var previewLanguage by remember { mutableStateOf(AppLanguage.NONE) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "“${quote.text}”",
            // FIXED: 'h5' -> 'headlineSmall'
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        val secondaryText = when (previewLanguage) {
            AppLanguage.HINDI -> quote.text_hi
            AppLanguage.URDU -> quote.text_ur
            else -> null
        }

        if (secondaryText != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF00695C),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "- ${quote.author}",
            fontStyle = FontStyle.Italic,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onLanguageSelected(AppLanguage.HINDI) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("English + Hindi")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onLanguageSelected(AppLanguage.URDU) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("English + Urdu")
        }
    }
}