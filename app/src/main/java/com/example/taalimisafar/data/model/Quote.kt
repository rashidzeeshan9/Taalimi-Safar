package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class Quote(
  //  @SerializedName("id") val id: Int,

    // 🔴 FIX: If backend says "quote", map it to "text" here
    @SerializedName("text") val text: String,

    // 🔴 FIX: If backend says "quote_hi", map it to "text_hi"
    @SerializedName("text_hi") val text_hi: String?,

    // 🔴 FIX: If backend says "quote_ur", map it to "text_ur"
    @SerializedName("text_ur") val text_ur: String?,

    @SerializedName("author") val author: String
)