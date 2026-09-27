package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasaagaovcps.TasaagaApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SchoolViewModel(tasaagaApplication().container.schoolRepository)
        }
        initializer {
            SupportViewModel(tasaagaApplication().container.supportRepository)
        }
        initializer {
            VolunteerViewModel(tasaagaApplication().container.volunteerRepository)
        }
        initializer {
            NewsViewModel(tasaagaApplication().container.newsRepository)
        }
        initializer {
            LoginViewModel(
                userRepository    = tasaagaApplication().container.userRepository,
                studentRepository = tasaagaApplication().container.studentRepository
            )
        }
        initializer {
            TeacherViewModel(
                attendanceRepository = tasaagaApplication().container.attendanceRepository,
                geminiRepository     = tasaagaApplication().container.geminiRepository
            )
        }
        initializer {
            FinanceViewModel(
                financeRepository = tasaagaApplication().container.financeRepository,
                geminiRepository  = tasaagaApplication().container.geminiRepository
            )
        }
        initializer {
            AdminViewModel(
                studentRepository      = tasaagaApplication().container.studentRepository,
                attendanceRepository   = tasaagaApplication().container.attendanceRepository,
                announcementRepository = tasaagaApplication().container.announcementRepository
            )
        }
        initializer {
            HeadteacherViewModel(
                studentRepository      = tasaagaApplication().container.studentRepository,
                attendanceRepository   = tasaagaApplication().container.attendanceRepository,
                staffRepository        = tasaagaApplication().container.staffRepository,
                financeRepository      = tasaagaApplication().container.financeRepository,
                announcementRepository = tasaagaApplication().container.announcementRepository
            )
        }
        initializer {
            BoardingViewModel(
                boardingRepository = tasaagaApplication().container.boardingRepository,
                studentRepository  = tasaagaApplication().container.studentRepository
            )
        }
        initializer {
            ParentViewModel(
                studentRepository      = tasaagaApplication().container.studentRepository,
                attendanceRepository   = tasaagaApplication().container.attendanceRepository,
                examResultRepository   = tasaagaApplication().container.examResultRepository,
                financeRepository      = tasaagaApplication().container.financeRepository,
                announcementRepository = tasaagaApplication().container.announcementRepository
            )
        }
        initializer {
            StudentViewModel(
                studentRepository      = tasaagaApplication().container.studentRepository,
                examResultRepository   = tasaagaApplication().container.examResultRepository,
                announcementRepository = tasaagaApplication().container.announcementRepository,
                attendanceRepository   = tasaagaApplication().container.attendanceRepository
            )
        }
    }
}

fun CreationExtras.tasaagaApplication(): TasaagaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TasaagaApplication)
