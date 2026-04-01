package com.example.taalimisafar.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.*
import com.example.taalimisafar.data.remote.RetrofitClient
import com.example.taalimisafar.utils.TokenManager
import kotlinx.coroutines.launch
import android.util.Log
import retrofit2.HttpException
class AuthViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getClient(context)

    // We strictly use TokenManager now, so SharedPreferences is removed from here!
    private val tokenManager = TokenManager(context)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var isOtpSent = mutableStateOf(false)

    var isAuthenticated = mutableStateOf(tokenManager.getAccessToken() != null)
    var userProfile = mutableStateOf<UserProfile?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                Log.e("AUTH_DEBUG", "1. Sending Login Request for $email...")

                val response = apiService.loginUser(LoginRequest(email, password))

                // 🔥 THE TRAP: Let's see what Android actually received!
                Log.e("AUTH_DEBUG", "2. Login Success! Access Token: >>${response.access}<<")

                if (response.access.isNullOrBlank()) {
                    errorMessage.value = "Error: Android received a blank token!"
                    isLoading.value = false
                    return@launch
                }

                tokenManager.saveTokens(response.access, response.refresh)

                Log.e("AUTH_DEBUG", "3. Attempting to fetch profile with new token...")
                val profile = apiService.getMyProfile("Bearer ${response.access}")

                Log.e("AUTH_DEBUG", "4. Profile fetched successfully!")
                userProfile.value = profile
                isAuthenticated.value = true

            } catch (e: HttpException) {
                // 🔥 THE TRAP: Print the EXACT error message from Django
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("AUTH_DEBUG", "HTTP Error ${e.code()}: $errorBody")

                if (e.code() == 401) {
                    errorMessage.value = "Auth error: Token was rejected."
                } else {
                    errorMessage.value = "Server error: ${e.code()}"
                }
                tokenManager.clearTokens()
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Crash: ${e.message}")
                errorMessage.value = "Network error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun signup(username: String, email: String, password: String) {
        // 🔥 1. Check if fields are empty before doing anything!
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage.value = "Please fill out all fields."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                apiService.registerUser(RegisterRequest(username, email, password))
                isOtpSent.value = true
                errorMessage.value = "OTP Sent to Email!"
            } catch (e: retrofit2.HttpException) {
                // 🔥 2. Handle specific backend error responses
                errorMessage.value = "Signup failed. Username or Email is already taken."
            } catch (e: Exception) {
                errorMessage.value = "Network error. Please try again."
            } finally {
                isLoading.value = false
            }
        }
    }

    fun verifyOtp(email: String, otpCode: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = apiService.verifyOtp(VerifyOtpRequest(email, otpCode))
                tokenManager.saveTokens(response.access, response.refresh)
                isAuthenticated.value = true
                isOtpSent.value = false
                fetchProfile()
            } catch (e: Exception) {
                errorMessage.value = "Invalid or expired OTP."
            } finally { isLoading.value = false }
        }
    }

    fun resendOtp(email: String) {
        viewModelScope.launch {
            try {
                apiService.resendOtp(mapOf("email" to email))
                errorMessage.value = "New OTP Sent!"
            } catch (e: Exception) {
                errorMessage.value = "Failed to resend. Try again."
            }
        }
    }

    // Call this to reset the screen state
    fun resetSignupState() {
        isOtpSent.value = false
        errorMessage.value = null
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getAccessToken()

                if (token != null) {
                    val profile = apiService.getMyProfile("Bearer $token")
                    userProfile.value = profile
                } else {
                    isAuthenticated.value = false
                }
            } catch (e: HttpException) {
                // 🔥 This catches the 401 error from Django
                if (e.code() == 401) {
                    tokenManager.clearTokens() // Destroy the dead token
                    isAuthenticated.value = false // Kick them to the Login screen
                    userProfile.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val accessToken = tokenManager.getAccessToken()
                val refreshToken = tokenManager.getRefreshToken()

                // You need to pass BOTH the Authorization header and the refresh token body
                if (accessToken != null && refreshToken != null) {
                    apiService.logoutUser("Bearer $accessToken", mapOf("refresh" to refreshToken))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                // Always clear the local session even if the server call fails
                tokenManager.clearTokens()
                isAuthenticated.value = false
                userProfile.value = null
            }
        }
    }
}