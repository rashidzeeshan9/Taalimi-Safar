package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Job
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient // 👈 Added this import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 👇 FIXED: Added "= RetrofitClient.api" so Android knows how to build it!
class JobViewModel(
    private val apiService: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Optional: Added error message state just like your Internship code
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchJobs(
        categoryId: Int? = null,
        searchQuery: String? = null,
        sector: String? = null,
        stateId: Int? = null,
        isRemote: Boolean? = null,
        experience: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _jobs.value = emptyList()
            try {
                val response = apiService.getJobsByCategory(
                    categoryId = categoryId,
                    searchQuery = searchQuery,
                    sector = sector,
                    stateId = stateId,
                    isRemote = isRemote,
                    experience = experience
                )
                _jobs.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load jobs: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}