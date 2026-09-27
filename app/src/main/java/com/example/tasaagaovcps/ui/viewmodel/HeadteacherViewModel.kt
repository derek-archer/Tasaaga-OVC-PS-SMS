package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.AnnouncementItem
import com.example.tasaagaovcps.data.model.ExpenseRecord
import com.example.tasaagaovcps.data.repository.AnnouncementRepository
import com.example.tasaagaovcps.data.repository.AttendanceRepository
import com.example.tasaagaovcps.data.repository.FinanceRepository
import com.example.tasaagaovcps.data.repository.StaffRepository
import com.example.tasaagaovcps.data.repository.StudentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HeadteacherDashboardState(
    val totalStudents: Int = 0,
    val attendanceRate: Double = 0.0,
    val activeStaff: Int = 0,
    val staffOnLeave: Int = 0,
    val pendingExpenses: List<ExpenseRecord> = emptyList(),
    val announcements: List<AnnouncementItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HeadteacherViewModel(
    private val studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val staffRepository: StaffRepository,
    private val financeRepository: FinanceRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HeadteacherDashboardState())
    val state: StateFlow<HeadteacherDashboardState> = _state.asStateFlow()

    fun load(schoolId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val studentsDeferred      = async { studentRepository.countStudents(schoolId) }
                val attendanceDeferred    = async { attendanceRepository.getSchoolAttendanceRate(schoolId) }
                val activeStaffDeferred   = async { staffRepository.countActiveStaff(schoolId) }
                val onLeaveDeferred       = async { staffRepository.countOnLeave(schoolId) }
                val expensesDeferred      = async { financeRepository.getRecentExpenses(schoolId, limit = 10) }
                val announcementsDeferred = async { announcementRepository.getPublishedAnnouncements(schoolId) }

                val pendingExpenses = expensesDeferred.await().filter { it.status == "Pending" }

                _state.value = HeadteacherDashboardState(
                    totalStudents   = studentsDeferred.await(),
                    attendanceRate  = attendanceDeferred.await(),
                    activeStaff     = activeStaffDeferred.await(),
                    staffOnLeave    = onLeaveDeferred.await(),
                    pendingExpenses = pendingExpenses,
                    announcements   = announcementsDeferred.await(),
                    isLoading       = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load dashboard: ${e.message}"
                )
            }
        }
    }
}
