package com.example.taalimisafar.data.model

data class Quote(
    val id: Int,
    val text: String,      // English
    val text_hi: String?,  // Hindi (Nullable)
    val text_ur: String?,  // Urdu (Nullable)
    val author: String
)