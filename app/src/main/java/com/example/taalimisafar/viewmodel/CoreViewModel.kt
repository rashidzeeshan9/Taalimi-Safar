package com.example.taalimisafar.viewmodel

import AboutUsResponse
import FeedbackRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoreViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _aboutUsData = MutableStateFlow<AboutUsResponse?>(null)
    val aboutUsData: StateFlow<AboutUsResponse?> = _aboutUsData

    private val _feedbackStatus = MutableStateFlow<String?>(null)
    val feedbackStatus: StateFlow<String?> = _feedbackStatus

    fun fetchAboutUs() {
        viewModelScope.launch {
            try {
                val response = apiService.getAboutUs()
                if (response.isSuccessful) {
                    _aboutUsData.value = response.body()
                }
            } catch (e: Exception) {
                // Ignore or log network errors
            }
        }
    }

    // 🔥 Now we just accept the token as a parameter!
    fun submitFeedback(token: String, message: String) {
        viewModelScope.launch {
            try {
                // Attach the Bearer prefix to the JWT token
                val response = apiService.submitFeedback("Bearer $token", FeedbackRequest(message))
                if (response.isSuccessful) {
                    _feedbackStatus.value = "Success"
                } else {
                    _feedbackStatus.value = "Failed to send. Please try again."
                }
            } catch (e: Exception) {
                _feedbackStatus.value = "Network Error. Check your connection."
            }
        }
    }

    fun resetFeedbackStatus() {
        _feedbackStatus.value = null
    }
}