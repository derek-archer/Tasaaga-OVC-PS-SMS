package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.SchoolInfo
import com.example.tasaagaovcps.data.repository.SchoolRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SchoolViewModel(
    private val repository: SchoolRepository
) : ViewModel() {
    val schoolInfo: StateFlow<SchoolInfo?> = repository.getSchoolInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
