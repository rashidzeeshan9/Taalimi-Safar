package com.example.taalimisafar.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.*
import com.example.taalimisafar.data.remote.RetrofitClient
import com.example.taalimisafar.utils.TokenManager
import kotlinx.coroutines.launch
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

    // When the app starts, if they are logged in, automatically fetch their data!
    init {
        if (isAuthenticated.value) {
            fetchProfile()
        }
    }
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
                val profile = apiService.getMyProfile()

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
                    val profile = apiService.getMyProfile()
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

    // ==========================================
    // 🔥 NEW: SAVE PROFILE EDITS TO BACKEND
    // ==========================================
    fun updateProfileData(updates: Map<String, String>) {
        viewModelScope.launch {
            try {
                // 1. Send the changes to the Django backend using PATCH
                val updatedProfile = apiService.updateProfile(updates)

                // 2. Update the local Compose state instantly
                userProfile.value = updatedProfile

                Log.d("AUTH_DEBUG", "Profile updated successfully: $updates")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("AUTH_DEBUG", "Failed to update profile HTTP ${e.code()}: $errorBody")
                errorMessage.value = "Failed to update profile."
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Error updating profile", e)
                errorMessage.value = "Network error while saving."
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val refreshToken = tokenManager.getRefreshToken()

                // Pass the refresh token body
                if (refreshToken != null) {
                    apiService.logoutUser(mapOf("refresh" to refreshToken))
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

    // 🔥 Request an OTP to be sent to the NEW email address
    fun requestEmailChangeOtp(newEmail: String) {
        viewModelScope.launch {
            try {
                // TODO: Create this endpoint in Django and ApiService
                // apiService.requestEmailUpdateOtp(mapOf("new_email" to newEmail))
                Log.d("AUTH_DEBUG", "Requested OTP for new email: $newEmail")
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Failed to request email OTP", e)
            }
        }
    }

    // 🔥 Verify the OTP. If true, the UI will update the email instantly.
    fun verifyEmailChangeOtp(newEmail: String, otp: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // TODO: Create this endpoint in Django and ApiService
                // val response = apiService.verifyEmailUpdateOtp(mapOf("new_email" to newEmail, "otp" to otp))

                // If successful, we update the profile data to reflect the new email
                updateProfileData(mapOf("email" to newEmail))
                onResult(true)
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Failed to verify email OTP", e)
                onResult(false)
            }
        }
    }
    fun getAccessToken(): String? {
        return tokenManager.getAccessToken()
    }
}