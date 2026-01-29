package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    // 1. ADD ERROR STATE
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    init {
        fetchCourses()
    }

    fun fetchCourses() {
        viewModelScope.launch {
            _errorMessage.value = null // Reset error
            try {
                // Log that we are trying to connect
                Log.d("API_DEBUG", "Attempting to fetch courses...")

                val response = RetrofitClient.api.getCourses()

                Log.d("API_DEBUG", "Success! Found ${response.size} courses")
                _courses.value = response

            } catch (e: Exception) {
                // 2. CAPTURE THE EXACT ERROR
                Log.e("API_DEBUG", "Error fetching courses", e)
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}