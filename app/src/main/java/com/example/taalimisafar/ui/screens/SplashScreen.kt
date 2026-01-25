package com.example.taalimisafar.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.QuoteViewModel

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    viewModel: QuoteViewModel = viewModel()
) {
    val context = LocalContext.current
    val quoteData by viewModel.quoteState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (quoteData == null) {

            CircularProgressIndicator()
        } else {
            QuoteScreen(
                quote = quoteData!!,
                onLanguageSelected = { language ->
                    LanguageManager.saveLanguage(context, language)
                    onNavigateToHome()
                }
            )
        }
    }
}