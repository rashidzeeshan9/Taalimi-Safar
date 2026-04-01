package com.example.taalimisafar.data.model

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
    val user: Int,
    val custom_username: String,
    val profile_pic: String?,
    val total_points: Int
)