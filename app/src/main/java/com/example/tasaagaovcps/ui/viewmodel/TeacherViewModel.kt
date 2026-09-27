package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.AttendanceRecord
import com.example.tasaagaovcps.data.repository.AttendanceRepository
import com.example.tasaagaovcps.data.repository.GeminiRepository
import com.example.tasaagaovcps.ui.components.GeminiInsightState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeacherViewModel(
    private val attendanceRepository: AttendanceRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _recentAttendance = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val recentAttendance: StateFlow<List<AttendanceRecord>> = _recentAttendance.asStateFlow()

    private val _attendanceInsight = MutableStateFlow<GeminiInsightState>(GeminiInsightState.Idle)
    val attendanceInsight: StateFlow<GeminiInsightState> = _attendanceInsight.asStateFlow()

    /** Load attendance for the teacher's class. Call once the classId is known. */
    fun loadAttendance(classId: String) {
        viewModelScope.launch {
            val records = attendanceRepository.getRecentAttendance(classId, limit = 30)
            _recentAttendance.value = records
        }
    }

    /** Generate an AI insight from the current attendance data. */
    fun generateAttendanceInsight() {
        val records = _recentAttendance.value
        if (records.isEmpty()) {
            _attendanceInsight.value = GeminiInsightState.Error(
                "No attendance data loaded yet. Check that students are enrolled in this class."
            )
            return
        }

        viewModelScope.launch {
            _attendanceInsight.value = GeminiInsightState.Loading

            val total   = records.size
            val present = records.count { it.status == "Present" }
            val absent  = records.count { it.status == "Absent" }
            val late    = records.count { it.status == "Late" }
            val rate    = if (total > 0) (present * 100 / total) else 0

            val prompt = """
                Here is recent attendance data for a Tasaaga Primary School class (last 30 records):
                - Total records: $total
                - Present: $present ($rate%)
                - Absent: $absent
                - Late: $late

                Provide a brief attendance analysis with:
                1. Overall trend (good/concerning/needs attention)
                2. One specific action the teacher should take
                3. Any flag if attendance is below 85%
                Keep it to 3 bullet points max.
            """.trimIndent()

            val result = geminiRepository.generateInsight(prompt)
            _attendanceInsight.value = GeminiInsightState.Success(result)
        }
    }

    /** Stream an answer to the teacher's free-form question. [onChunk] is called per token. */
    suspend fun streamAnswer(question: String, onChunk: (String) -> Unit) {
        val records = _recentAttendance.value
        val contextPrompt = """
            Context: You are assisting a teacher at Tasaaga OVC Primary School.
            Current class attendance rate: ${
            if (records.isNotEmpty()) "${records.count { it.status == "Present" } * 100 / records.size}%"
            else "unknown"
        }

            Teacher question: $question
        """.trimIndent()

        geminiRepository.streamInsight(contextPrompt).collect { chunk ->
            onChunk(chunk)
        }
    }
}
