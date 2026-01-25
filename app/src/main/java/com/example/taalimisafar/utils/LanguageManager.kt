package com.example.taalimisafar.utils


import android.content.Context
import androidx.compose.runtime.mutableStateOf

object LanguageManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANG = "selected_language"


    // Default is NONE (English Only)
    var currentLanguage = mutableStateOf(AppLanguage.NONE)

    // Load saved language when app starts
    fun loadLanguage(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedCode = prefs.getString(KEY_LANG, AppLanguage.NONE.code) ?: AppLanguage.NONE.code

        // Find the matching Enum for the saved code
        currentLanguage.value = AppLanguage.values().find { it.code == savedCode } ?: AppLanguage.NONE
    }

    // Save user's choice
    fun saveLanguage(context: Context, language: AppLanguage) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, language.code).apply()

        // Update the live variable immediately
        currentLanguage.value = language
    }
}