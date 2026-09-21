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
            LoginViewModel(tasaagaApplication().container.userRepository)
        }
    }
}

fun CreationExtras.tasaagaApplication(): TasaagaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TasaagaApplication)
