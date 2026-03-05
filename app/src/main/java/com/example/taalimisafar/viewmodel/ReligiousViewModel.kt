package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.ReligiousProgram
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReligiousViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _programs = MutableStateFlow<List<ReligiousProgram>>(emptyList())
    val programs: StateFlow<List<ReligiousProgram>> = _programs

    private val _selectedProgram = MutableStateFlow<ReligiousProgram?>(null)
    val selectedProgram: StateFlow<ReligiousProgram?> = _selectedProgram

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPrograms(religionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _programs.value = api.getReligiousPrograms(religionId = religionId)
            } catch (e: Exception) {
                Log.e("ReligiousViewModel", "API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProgram.value = api.getReligiousProgramDetail(programId)
            } catch (e: Exception) {
                Log.e("ReligiousViewModel", "Detail Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}