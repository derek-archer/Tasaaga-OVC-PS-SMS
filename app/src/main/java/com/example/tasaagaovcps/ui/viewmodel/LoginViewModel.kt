package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.repository.StudentRepository
import com.example.tasaagaovcps.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Initial : LoginUiState
    object Loading : LoginUiState
    data class Success(
        val role: String,
        val schoolId: String,
        val profileId: String,
        val studentId: Int? = null,
        val classId: Int? = null
    ) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val profile = userRepository.login(email, password)
                if (profile != null) {
                    val schoolId = profile.schoolId ?: ""

                    // For Student and Parent roles, look up the linked student record
                    var studentId: Int? = null
                    var classId: Int? = null

                    when (profile.role) {
                        "Student" -> {
                            val student = studentRepository.getStudentByProfile(profile.id)
                            studentId = student?.id
                            classId   = student?.classId
                        }
                        "Parent" -> {
                            // Parents are linked to a student via profile_id on the student record
                            val student = studentRepository.getStudentByProfile(profile.id)
                            studentId = student?.id
                            classId   = student?.classId
                        }
                    }

                    _uiState.value = LoginUiState.Success(
                        role      = profile.role,
                        schoolId  = schoolId,
                        profileId = profile.id,
                        studentId = studentId,
                        classId   = classId
                    )
                } else {
                    _uiState.value = LoginUiState.Error("Profile not found or role missing")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.localizedMessage ?: "Login failed")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Initial
    }
}
