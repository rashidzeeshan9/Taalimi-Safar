package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.DiplomaProgram
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiplomaViewModel(private val api: ApiService = RetrofitClient.api) : ViewModel() {
    private val _programs = MutableStateFlow<List<DiplomaProgram>>(emptyList())
    val programs: StateFlow<List<DiplomaProgram>> = _programs

    private val _selectedProgram = MutableStateFlow<DiplomaProgram?>(null)
    val selectedProgram: StateFlow<DiplomaProgram?> = _selectedProgram

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPrograms(categoryId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _programs.value = api.getDiplomaPrograms(categoryId = categoryId)
            } catch (e: Exception) {
                Log.e("DiplomaVM", "Error fetching: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProgram.value = api.getDiplomaProgramDetail(programId)
            } catch (e: Exception) {
                Log.e("DiplomaVM", "Error fetching detail: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}