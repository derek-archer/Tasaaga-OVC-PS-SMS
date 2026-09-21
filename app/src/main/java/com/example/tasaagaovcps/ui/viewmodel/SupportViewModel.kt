package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.DonationInfo
import com.example.tasaagaovcps.data.repository.SupportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SupportViewModel(
    private val repository: SupportRepository
) : ViewModel() {
    val donationInfo: StateFlow<DonationInfo?> = repository.getDonationInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
