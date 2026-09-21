package com.example.tasaagaovcps.data

import com.example.tasaagaovcps.data.repository.*

interface AppContainer {
    val schoolRepository: SchoolRepository
    val supportRepository: SupportRepository
    val volunteerRepository: VolunteerRepository
    val newsRepository: NewsRepository
}

class AppContainerImpl : AppContainer {
    override val schoolRepository: SchoolRepository by lazy { SchoolRepositoryImpl() }
    override val supportRepository: SupportRepository by lazy { SupportRepositoryImpl() }
    override val volunteerRepository: VolunteerRepository by lazy { VolunteerRepositoryImpl() }
    override val newsRepository: NewsRepository by lazy { NewsRepositoryImpl() }
}
