package com.example.taalimisafar.ui.components


import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager

@Composable
fun DualLangText(
    english: String,
    hindi: String?,  // Nullable because backend might be empty
    urdu: String?,   // Nullable
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null
) {
    // 1. Check what the user selected (Hindi or Urdu)
    val selectedLanguage = LanguageManager.currentLanguage.value

    // 2. Pick the second language text
    val secondText = when (selectedLanguage) {
        AppLanguage.HINDI -> hindi
        AppLanguage.URDU -> urdu
        else -> null
    }

    // 3. Combine them automatically
    val finalText = if (!secondText.isNullOrBlank()) {
        "$english\n$secondText" // English on top, chosen language below
    } else {
        english // Fallback to just English
    }

    // 4. Render the Text
    Text(
        text = finalText,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight
    )
}