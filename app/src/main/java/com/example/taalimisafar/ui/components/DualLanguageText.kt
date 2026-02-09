package com.example.taalimisafar.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager

@Composable
fun DualLangText(
    english: String,
    hindi: String?,  // Nullable
    urdu: String?,   // Nullable
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null
) {
    val selectedLanguage = LanguageManager.currentLanguage.value

    // 1. Determine the second text
    val secondText = when (selectedLanguage) {
        AppLanguage.HINDI -> hindi
        AppLanguage.URDU -> urdu
        else -> null
    }

    // 2. Build the Styled Text
    val finalText = buildAnnotatedString {
        // Part A: English (Default Style)
        append(english)

        // Part B: Native Language (Only if valid and NOT a duplicate)
        if (!secondText.isNullOrBlank() && !secondText.equals(english, ignoreCase = true)) {
            append("\n") // New Line

            // Apply different style for the translation
            withStyle(
                style = SpanStyle(
                    // Make it slightly smaller relative to the English text
                    fontSize = (style.fontSize.value * 0.95).sp,

                    // ✅ FIXED: Changed Orange to Professional Dark Blue
                    color = if (selectedLanguage == AppLanguage.HINDI) Color(0xFF283593) else Color(0xFF00695C),

                    fontWeight = FontWeight.Normal // Keep translation weight normal
                )
            ) {
                append(secondText)
            }
        }
    }

    // 3. Render
    Text(
        text = finalText,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight
    )
}