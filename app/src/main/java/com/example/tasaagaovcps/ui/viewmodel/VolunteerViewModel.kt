package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.VolunteerOpportunity
import com.example.tasaagaovcps.data.repository.VolunteerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SubmissionState {
    object Idle : SubmissionState
    object Loading : SubmissionState
    object Success : SubmissionState
    data class Error(val message: String) : SubmissionState
}

class VolunteerViewModel(
    private val repository: VolunteerRepository
) : ViewModel() {
    val opportunities: StateFlow<List<VolunteerOpportunity>> = repository.getOpportunities()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedOpportunity = MutableStateFlow<VolunteerOpportunity?>(null)
    val selectedOpportunity: StateFlow<VolunteerOpportunity?> = _selectedOpportunity.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    fun selectOpportunity(opportunity: VolunteerOpportunity) {
        _selectedOpportunity.value = opportunity
        _submissionState.value = SubmissionState.Idle
    }

    fun clearSelectedOpportunity() {
        _selectedOpportunity.value = null
        _submissionState.value = SubmissionState.Idle
    }

    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }

    fun submitApplication(name: String, email: String, message: String) {
        val opportunity = _selectedOpportunity.value ?: return
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                repository.sendApplication(
                    opportunityTitle = opportunity.title,
                    name = name,
                    email = email,
                    message = message
                )
                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}
