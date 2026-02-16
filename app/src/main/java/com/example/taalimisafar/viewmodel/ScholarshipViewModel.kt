package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Scholarship
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScholarshipViewModel : ViewModel() {

    private val _scholarships = MutableStateFlow<List<Scholarship>>(emptyList())
    val scholarships: StateFlow<List<Scholarship>> = _scholarships.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _selectedScholarship = MutableStateFlow<Scholarship?>(null)
    val selectedScholarship: StateFlow<Scholarship?> = _selectedScholarship.asStateFlow()
    fun setSelectedScholarship(scholarship: Scholarship) {
        _selectedScholarship.value = scholarship
    }

    fun fetchScholarships(categoryId: Int, typeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getScholarships(categoryId, typeId)
                _scholarships.value = response
            } catch (e: Exception) {
                _scholarships.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}