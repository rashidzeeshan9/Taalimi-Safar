package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.SkillProgram
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SkillViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _programs = MutableStateFlow<List<SkillProgram>>(emptyList())
    val programs: StateFlow<List<SkillProgram>> = _programs
    private val _selectedProgram = MutableStateFlow<SkillProgram?>(null)
    val selectedProgram: StateFlow<SkillProgram?> = _selectedProgram
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun fetchPrograms(categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _programs.value = api.getSkillPrograms(categoryId = categoryId)
            } catch (e: Exception) {
                Log.e("SkillViewModel", "API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getSkillProgramDetail(programId)
                _selectedProgram.value = response
            } catch (e: Exception) {
                Log.e("SkillViewModel", "CRASH REASON: ${e.localizedMessage}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}