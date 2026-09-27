package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.AnnouncementItem
import com.example.tasaagaovcps.data.model.ExamResult
import com.example.tasaagaovcps.data.model.PaymentRecord
import com.example.tasaagaovcps.data.model.Student
import com.example.tasaagaovcps.data.repository.AnnouncementRepository
import com.example.tasaagaovcps.data.repository.AttendanceRepository
import com.example.tasaagaovcps.data.repository.ExamResultRepository
import com.example.tasaagaovcps.data.repository.FinanceRepository
import com.example.tasaagaovcps.data.repository.StudentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ParentDashboardState(
    val student: Student? = null,
    val attendanceRate: Double = 0.0,
    val examResults: List<ExamResult> = emptyList(),
    val payments: List<PaymentRecord> = emptyList(),
    val totalFeesPaid: Double = 0.0,
    val announcements: List<AnnouncementItem> = emptyList(),
    val classPosition: Int? = null,
    val classTotalStudents: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ParentViewModel(
    private val studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val examResultRepository: ExamResultRepository,
    private val financeRepository: FinanceRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParentDashboardState())
    val state: StateFlow<ParentDashboardState> = _state.asStateFlow()

    fun load(schoolId: String, studentId: Int? = null) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val announcementsDeferred = async {
                    announcementRepository.getAnnouncements(schoolId, "Parent")
                }

                if (studentId != null) {
                    val resultsDeferred  = async { examResultRepository.getResultsByStudent(studentId) }
                    val paymentsDeferred = async { financeRepository.getPaymentsByStudent(studentId.toString()) }

                    val results  = resultsDeferred.await()
                    val payments = paymentsDeferred.await()

                    _state.value = ParentDashboardState(
                        examResults     = results,
                        payments        = payments,
                        totalFeesPaid   = payments.sumOf { it.amount },
                        announcements   = announcementsDeferred.await(),
                        isLoading       = false
                    )
                } else {
                    _state.value = ParentDashboardState(
                        announcements = announcementsDeferred.await(),
                        isLoading     = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load parent portal: ${e.message}"
                )
            }
        }
    }
}
