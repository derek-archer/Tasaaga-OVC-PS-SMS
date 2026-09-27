package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.AnnouncementItem
import com.example.tasaagaovcps.data.model.Student
import com.example.tasaagaovcps.data.repository.AnnouncementRepository
import com.example.tasaagaovcps.data.repository.AttendanceRepository
import com.example.tasaagaovcps.data.repository.StudentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminDashboardState(
    val students: List<Student> = emptyList(),
    val totalStudents: Int = 0,
    val boysCount: Int = 0,
    val girlsCount: Int = 0,
    val dayCount: Int = 0,
    val boardingCount: Int = 0,
    val attendanceRate: Double = 0.0,
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val announcements: List<AnnouncementItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class AdminViewModel(
    private val studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    // Default school ID — in production this comes from the logged-in user's profile.school_id
    private val schoolId = "tasaaga-school-id"

    fun load(schoolId: String = this.schoolId) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val studentsDeferred     = async { studentRepository.getStudentsBySchool(schoolId) }
                val attendanceDeferred   = async { attendanceRepository.getSchoolAttendanceRate(schoolId) }
                val announcementsDeferred = async { announcementRepository.getPublishedAnnouncements(schoolId) }

                val students      = studentsDeferred.await()
                val attendanceRate = attendanceDeferred.await()
                val announcements  = announcementsDeferred.await()

                val genderMap = students.groupBy { it.gender }.mapValues { it.value.size }
                val typeMap   = students.groupBy { it.studentType }.mapValues { it.value.size }

                // Approximate present/absent from rate
                val total   = students.size
                val present = (total * attendanceRate / 100).toInt()
                val absent  = total - present

                _state.value = AdminDashboardState(
                    students       = students,
                    totalStudents  = total,
                    boysCount      = genderMap["Male"] ?: genderMap["Boy"] ?: 0,
                    girlsCount     = genderMap["Female"] ?: genderMap["Girl"] ?: 0,
                    dayCount       = typeMap["Day"] ?: 0,
                    boardingCount  = typeMap["Boarding"] ?: 0,
                    attendanceRate = attendanceRate,
                    presentCount   = present,
                    absentCount    = absent,
                    announcements  = announcements,
                    isLoading      = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load dashboard: ${e.message}"
                )
            }
        }
    }

    fun addStudent(student: Student, onDone: () -> Unit) {
        viewModelScope.launch {
            studentRepository.insertStudent(student)
            load() // Refresh from DB
            onDone()
        }
    }
}
