package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class VerifyOtpRequest(val email: String, val otp: String)

data class AuthResponse(
    val access: String,
    val refresh: String,
    val message: String? = null
)

data class UserProfile(
    val id: Int,

    // Note: Django's UserProfile model connects to User via a OneToOneField.
    // Depending on your serializer, this might come through as an Int (the user ID) or might not be needed.
    val user: Int? = null,

    val email: String? = null,
    val custom_username: String,
    val full_name: String? = null,

    // 🔥 THE FIXES: Translating Django's names to your Android variables!
    @SerializedName("dob")
    val date_of_birth: String? = null,

    @SerializedName("phone")
    val phone_number: String? = null,

    val gender: String? = null,

    @SerializedName("qualification")
    val qualification: String? = null,

    // Optional: If you didn't add degree to Django, Android will just ignore this safely.
    val degree: String? = null,

    val profile_pic: String? = null,
    val total_points: Int = 0,
    val days_since_last_change: Int? = null
)