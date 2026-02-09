package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class Scholarship(
    val id: Int,
    val title: String,
    val provider: String,
    val amount: String,

    // ✅ These are the fields causing your "Unresolved Reference" errors
    val status: String,
    val description: String,
    val eligibility: String,

    // JSON Mapping (Snake_case from Django -> CamelCase in Kotlin)
    @SerializedName("application_start") val applicationStart: String,
    @SerializedName("last_date") val lastDate: String,      // Fixes 'lastDate' error
    @SerializedName("website_link") val websiteLink: String?, // Fixes 'websiteLink' error
    val image: String?,

    // Translations
    @SerializedName("description_hi") val descriptionHi: String?,
    @SerializedName("eligibility_hi") val eligibilityHi: String?,
    @SerializedName("description_ur") val descriptionUr: String?,
    @SerializedName("eligibility_ur") val eligibilityUr: String?
)