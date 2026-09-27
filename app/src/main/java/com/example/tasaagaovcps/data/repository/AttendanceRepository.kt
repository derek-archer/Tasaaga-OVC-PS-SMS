package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.AttendanceRecord
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.LocalDate

interface AttendanceRepository {
    suspend fun getAttendanceByClass(classId: String, date: LocalDate): List<AttendanceRecord>
    suspend fun getRecentAttendance(classId: String, limit: Int = 30): List<AttendanceRecord>
    suspend fun getSchoolAttendanceRate(schoolId: String): Double
}

class AttendanceRepositoryImpl(
    private val client: SupabaseClient
) : AttendanceRepository {

    override suspend fun getAttendanceByClass(
        classId: String,
        date: LocalDate
    ): List<AttendanceRecord> {
        return try {
            client.postgrest["attendance"]
                .select {
                    filter {
                        eq("class_id", classId)
                        eq("date", date.toString())
                    }
                }
                .decodeList<AttendanceRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getRecentAttendance(classId: String, limit: Int): List<AttendanceRecord> {
        return try {
            client.postgrest["attendance"]
                .select {
                    filter { eq("class_id", classId) }
                    order("date", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<AttendanceRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getSchoolAttendanceRate(schoolId: String): Double {
        return try {
            val records = client.postgrest["attendance"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("date", Order.DESCENDING)
                    limit(500L)
                }
                .decodeList<AttendanceRecord>()
            if (records.isEmpty()) return 0.0
            val present = records.count { it.status == "Present" }
            (present.toDouble() / records.size) * 100.0
        } catch (e: Exception) {
            0.0
        }
    }
}
