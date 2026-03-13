package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Stream
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AcademicViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    // ----------------------------
    // STREAM LIST
    // ----------------------------

    private val _streams = MutableStateFlow<List<Stream>>(emptyList())
    val streams: StateFlow<List<Stream>> = _streams


    // ----------------------------
    // COURSES LIST
    // ----------------------------

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses


    // ----------------------------
    // SELECTED COURSE DETAIL
    // ----------------------------

    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse


    // ----------------------------
    // LOADING STATE
    // ----------------------------

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    // ----------------------------
    // FETCH STREAMS
    // ----------------------------

    fun fetchStreams() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _streams.value = api.getStreams()
            } catch (e: Exception) {
                Log.e("AcademicVM", "Error fetching streams: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ----------------------------
    // FETCH COURSES BY STREAM
    // ----------------------------

    fun fetchCourses(streamId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _courses.value = api.getCourses(streamId = streamId)
            } catch (e: Exception) {
                Log.e("AcademicVM", "Error fetching courses: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ----------------------------
    // FETCH COURSE DETAIL
    // ----------------------------

    fun fetchCourseDetail(courseId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedCourse.value = api.getCourseDetail(courseId)
            } catch (e: Exception) {
                Log.e("AcademicVM", "Error fetching course detail: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ----------------------------
    // CLEAR SELECTED COURSE
    // ----------------------------

    fun clearSelectedCourse() {
        _selectedCourse.value = null
    }
}