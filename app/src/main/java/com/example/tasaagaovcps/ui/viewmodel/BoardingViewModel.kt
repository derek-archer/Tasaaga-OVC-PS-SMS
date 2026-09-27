package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.BoardingDorm
import com.example.tasaagaovcps.data.model.WelfareIncident
import com.example.tasaagaovcps.data.repository.BoardingRepository
import com.example.tasaagaovcps.data.repository.StudentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BoardingDashboardState(
    val dorms: List<BoardingDorm> = emptyList(),
    val welfareIncidents: List<WelfareIncident> = emptyList(),
    val totalBoarders: Int = 0,
    val boysCount: Int = 0,
    val girlsCount: Int = 0,
    val totalCapacity: Int = 0,
    val totalOccupied: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

class BoardingViewModel(
    private val boardingRepository: BoardingRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BoardingDashboardState())
    val state: StateFlow<BoardingDashboardState> = _state.asStateFlow()

    fun load(schoolId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val dormsDeferred    = async { boardingRepository.getDorms(schoolId) }
                val incidentsDeferred = async { boardingRepository.getWelfareIncidents(schoolId, 20) }
                val boardersDeferred  = async { studentRepository.getBoardingStudents(schoolId) }

                val dorms     = dormsDeferred.await()
                val incidents = incidentsDeferred.await()
                val boarders  = boardersDeferred.await()

                val boys  = boarders.count { it.gender == "Male" || it.gender == "Boy" }
                val girls = boarders.count { it.gender == "Female" || it.gender == "Girl" }

                _state.value = BoardingDashboardState(
                    dorms            = dorms,
                    welfareIncidents = incidents,
                    totalBoarders    = boarders.size,
                    boysCount        = boys,
                    girlsCount       = girls,
                    totalCapacity    = dorms.sumOf { it.capacity },
                    totalOccupied    = dorms.sumOf { it.occupied },
                    isLoading        = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load boarding data: ${e.message}"
                )
            }
        }
    }

    fun reportIncident(studentName: String, incidentType: String, description: String, reportedBy: String = "Boarding Officer") {
        viewModelScope.launch {
            val now = java.time.LocalDate.now().toString()
            val incident = WelfareIncident(
                studentName = studentName,
                incidentType = incidentType,
                description = description,
                date = now,
                reportedBy = reportedBy
            )
            boardingRepository.insertWelfareIncident(incident)
            _state.value = _state.value.copy(
                welfareIncidents = listOf(incident) + _state.value.welfareIncidents
            )
        }
    }
}
