package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.AnnouncementItem
import com.example.tasaagaovcps.data.model.ExamResult
import com.example.tasaagaovcps.data.model.Student
import com.example.tasaagaovcps.data.repository.AnnouncementRepository
import com.example.tasaagaovcps.data.repository.AttendanceRepository
import com.example.tasaagaovcps.data.repository.ExamResultRepository
import com.example.tasaagaovcps.data.repository.StudentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StudentDashboardState(
    val student: Student? = null,
    val attendanceRate: Double = 0.0,
    val examResults: List<ExamResult> = emptyList(),
    val classPosition: Int? = null,
    val classTotalStudents: Int = 0,
    val announcements: List<AnnouncementItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class StudentViewModel(
    private val studentRepository: StudentRepository,
    private val examResultRepository: ExamResultRepository,
    private val announcementRepository: AnnouncementRepository,
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StudentDashboardState())
    val state: StateFlow<StudentDashboardState> = _state.asStateFlow()

    fun load(schoolId: String, studentId: Int? = null, classId: Int? = null) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val announcementsDeferred = async {
                    announcementRepository.getAnnouncements(schoolId, "Student")
                }

                if (studentId != null) {
                    val resultsDeferred = async { examResultRepository.getResultsByStudent(studentId) }
                    val classResultsDeferred = if (classId != null) {
                        async { examResultRepository.getResultsByClass(classId) }
                    } else null

                    val myResults    = resultsDeferred.await()
                    val classResults = classResultsDeferred?.await() ?: emptyList()

                    // Compute class rank by total marks
                    val myTotal = myResults.sumOf { it.marks }
                    val studentTotals = classResults
                        .groupBy { it.studentId }
                        .mapValues { (_, results) -> results.sumOf { it.marks } }
                        .values
                        .sortedDescending()

                    val rank = studentTotals.indexOf(myTotal).takeIf { it >= 0 }?.plus(1)

                    _state.value = StudentDashboardState(
                        examResults         = myResults,
                        classPosition       = rank,
                        classTotalStudents  = studentTotals.size,
                        announcements       = announcementsDeferred.await(),
                        isLoading           = false
                    )
                } else {
                    _state.value = StudentDashboardState(
                        announcements = announcementsDeferred.await(),
                        isLoading     = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load student portal: ${e.message}"
                )
            }
        }
    }
}
