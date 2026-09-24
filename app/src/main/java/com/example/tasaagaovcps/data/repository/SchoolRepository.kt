package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.SchoolInfo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SchoolRepository {
    fun getSchoolInfo(): Flow<SchoolInfo>
}

class SchoolRepositoryImpl(private val supabaseClient: SupabaseClient) : SchoolRepository {
    private val defaultSchoolInfo = SchoolInfo(
        mission = "Rising To Succeed, self-reliance.",
        motto = "Rising To Succeed",
        programs = listOf(
            "Primary School (Day & Boarding)",
            "Secondary School",
            "Vocational Training"
        )
    )

    override fun getSchoolInfo(): Flow<SchoolInfo> = flow {
        val remoteInfo = try {
            supabaseClient.postgrest.from("school_info").select().decodeSingleOrNull<SchoolInfo>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        emit(remoteInfo ?: defaultSchoolInfo)
    }
}
