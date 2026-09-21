package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.SchoolInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface SchoolRepository {
    fun getSchoolInfo(): Flow<SchoolInfo>
}

class SchoolRepositoryImpl : SchoolRepository {
    override fun getSchoolInfo(): Flow<SchoolInfo> = flowOf(
        SchoolInfo(
            mission = "Rising To Succeed, self-reliance.",
            motto = "Rising To Succeed",
            programs = listOf(
                "Primary School (Day & Boarding)",
                "Secondary School",
                "Vocational Training"
            )
        )
    )
}
