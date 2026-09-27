package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ContactUiState {
    data object Idle : ContactUiState
    data object Sending : ContactUiState
    data object Success : ContactUiState
    data class Error(val message: String) : ContactUiState
}

class ContactViewModel(private val contactRepository: ContactRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ContactUiState>(ContactUiState.Idle)
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun sendInquiry(name: String, email: String, phone: String, subject: String, message: String) {
        viewModelScope.launch {
            _uiState.value = ContactUiState.Sending
            try {
                contactRepository.sendInquiry(name, email, phone, subject, message)
                _uiState.value = ContactUiState.Success
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(
                    e.message ?: "Failed to send message. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ContactUiState.Idle
    }
}
