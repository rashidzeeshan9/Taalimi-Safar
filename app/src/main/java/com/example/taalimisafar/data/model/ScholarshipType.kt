package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class ScholarshipType(
    val id: Int,
    val name: String,
    @SerializedName("name_hi") val nameHi: String?,
    @SerializedName("name_ur") val nameUr: String?,
    val image: String?
)